package ru.netology.statsview.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.content.withStyledAttributes
import ru.netology.statsview.R
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(context: Context,
attrs: AttributeSet? = null) : View(context,attrs) {

    private var center = PointF(0F, 0F)
    private var radius = 0F
    private var linedWidth = 0
    private var colors = emptyList<Int>()
    private var progress = 0F
    private var progress1 = 0F
    private var animator: Animator? = null
    var prog:FloatArray? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.STROKE
        textAlign = Paint.Align.CENTER
    }
    private var circle = RectF(0F, 0F,0F,0F)
    var data: List<Float> = emptyList()
       set(value) {
          field = value
           update()
    }

    private fun update() {
        animator?.let{
            it.removeAllListeners()
            it.cancel()
        }
        val prog1 = FloatArray(data.size)
        animator = ValueAnimator.ofFloat(0F, 4F).apply {
            addUpdateListener {
                progress = it.animatedValue as Float
                for(i in 0 ..  prog1.size -1 ){
                    if(progress < i){
                        prog1[i]=0F}
                    else{
                        if(progress > i + 1)
                            prog1[i]=1F
                        else
                         prog1[i] = progress - i}
                }
                prog = prog1
                invalidate()
            }
            duration = 5_000
            start()
        }

    }

init{
    context.withStyledAttributes(attrs, R.styleable.StatsView){
        textPaint.textSize = getDimension(R.styleable.StatsView_textSize,
            AndroidUtils.dp(context, 40F).toFloat())

        linedWidth = getDimensionPixelSize(R.styleable.StatsView_lineWidth,
            AndroidUtils.dp(context, 10F))

        paint.strokeWidth = linedWidth.toFloat()

        colors = listOf(
            getColor(R.styleable.StatsView_color1, getRandomColor()),
            getColor(R.styleable.StatsView_color2, getRandomColor()),
            getColor(R.styleable.StatsView_color3, getRandomColor()),
            )
    }
}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center = PointF(w/2F, h/2F)
        radius = min(w,h)/2F - linedWidth/2F
        circle = RectF(
               center.x - radius,
               center.y - radius,
              center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if(data.isEmpty())
            return
        var startAngle = -90F
        var addAngle = startAngle

        data.forEachIndexed { index,item ->
            val angle = item * 360F
//  **********************   Задача Not Filled ********************
//            if(index.equals(data.lastIndex)){
//                paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_OVER))
//            } else{
//                paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC))
//            }
            paint.color = colors.getOrElse(index) {getRandomColor()}
            canvas.drawArc(circle, startAngle, angle * (prog?.let{it[index]} ?: 0F), false, paint)
            if(index == 0){
                addAngle = angle/2
            }
            startAngle += angle
        }
        paint.color = colors.getOrElse(0) {getRandomColor()}
        canvas.drawArc(circle, startAngle, addAngle , false, paint)
        canvas.drawText(
            "%.2f%%".format(data.sum()*100),
            center.x,
            center.y - textPaint.textSize / 4,
            textPaint
        )
    }
    private fun getRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}