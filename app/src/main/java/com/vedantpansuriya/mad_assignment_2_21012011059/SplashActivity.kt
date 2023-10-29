package com.vedantpansuriya.mad_assignment_2_21012011059

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.splashImage)
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_zoom)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // Animation started (you can add code here if needed)
            }

            override fun onAnimationEnd(animation: Animation) {
                // Animation ended, start MainActivity
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // Close the splash screen activity
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Animation repeated (if needed)
            }
        })

        imageView.startAnimation(animation)
    }
}