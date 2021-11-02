package ru.netology.statsview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.statsview.utils.StatsView
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<StatsView>(R.id.statsView)
//        val runnable = object : Runnable {
//            override fun run(){
                val data = listOf(600F, 400F, Random.nextFloat() * 700F,  500F)
                val dataSum =  data.sum()
                view.data = data.map{it/dataSum}

        val label = findViewById<TextView>(R.id.label)
//        val button = findViewById<Button>(R.id.startButton)
        val viewAnim = AnimationUtils.loadAnimation(
            this, R.anim.view_animation
        ).apply {
            setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                    label.text = " started"
                }

                override fun onAnimationEnd(animation: Animation?) {
                    label.text = " ended"
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    label.text = " repeat"
                }
            })
        }
//        button.startAnimation(viewAnim)
//        button.setOnClickListener {
//            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
//        }


//                Handler(Looper.getMainLooper()).postDelayed(this, 2_500)
//            }
//        }
//        Handler(Looper.getMainLooper()).postDelayed(runnable, 2_500)
    }
}