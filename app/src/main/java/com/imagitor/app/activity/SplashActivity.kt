package com.imagitor.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.imagitor.app.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val splashHandler = Runnable {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(splashHandler, DELAY)
    }

    override fun onPause() {
        handler.removeCallbacks(splashHandler)
        super.onPause()
    }

    companion object {
        const val DELAY: Long = 2000
    }
}