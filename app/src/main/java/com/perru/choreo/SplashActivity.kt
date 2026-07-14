package com.perru.choreo


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Wait 2 seconds, then decide where to go
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("choreo_prefs", MODE_PRIVATE)
            val hasSeenOnboarding = prefs.getBoolean("onboarding_complete", false)

            if (hasSeenOnboarding) {
                // Returning user → go straight to Home
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // First time → go to Onboarding
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish() // Remove splash from back stack
        }, 2000)
    }
}