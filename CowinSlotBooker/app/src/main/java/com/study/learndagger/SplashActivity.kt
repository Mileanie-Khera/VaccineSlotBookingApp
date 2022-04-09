package com.study.learndagger

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val mNextStep = Runnable {
        performRunnableActions()
    }

    private fun performRunnableActions() {
        startActivity(Intent(this, CowinHomeActivity::class.java))
        finish()
    }

    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        mHandler.postDelayed(mNextStep, 500)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mNextStep)
    }
}