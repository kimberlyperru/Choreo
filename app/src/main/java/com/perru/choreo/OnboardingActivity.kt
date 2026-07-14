package com.perru.choreo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var dotsIndicator: LinearLayout

    private val slides = listOf(
        OnboardItem(
            emoji = "🎵",
            title = "Welcome to Choreo",
            description = "Learn any instrument by following along with real sheet music — at your own pace."
        ),
        OnboardItem(
            emoji = "🎯",
            title = "Play at your level",
            description = "Whether you're a complete beginner or brushing up on skills, Choreo adapts to you."
        ),
        OnboardItem(
            emoji = "🎹",
            title = "Pick your instrument",
            description = "Piano, guitar, violin and more. Switch instruments anytime for any song."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        dotsIndicator = findViewById(R.id.dotsIndicator)

        viewPager.adapter = OnboardingAdapter(slides)

        setupDots()
        updateDots(0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
                btnNext.text = if (position == slides.size - 1) "Get Started" else "Next"
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < slides.size - 1) {
                viewPager.currentItem++
            } else {
                finishOnboarding()
            }
        }
    }

    private fun setupDots() {
        dotsIndicator.removeAllViews()
        slides.forEach { _ ->
            val dot = ImageView(this)
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_inactive))
            val params = LinearLayout.LayoutParams(12, 12).apply {
                setMargins(6, 0, 6, 0)
            }
            dot.layoutParams = params
            dotsIndicator.addView(dot)
        }
    }

    private fun updateDots(activeIndex: Int) {
        for (i in 0 until dotsIndicator.childCount) {
            val dot = dotsIndicator.getChildAt(i) as ImageView
            dot.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (i == activeIndex) R.drawable.dot_active else R.drawable.dot_inactive
                )
            )
        }
    }

    private fun finishOnboarding() {
        getSharedPreferences("choreo_prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_complete", true)
            .apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}