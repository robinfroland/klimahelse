package com.example.helse

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.helse.adapters.OnboardingAdapter
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.Preferences
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setFullscreen()

        viewPager.adapter = OnboardingAdapter(supportFragmentManager)

        updateDotIndicator(0)
        viewPager.setOnPageListener()

        firstDot.setOnClickListener {
            viewPager.currentItem = 0
        }

        secondDot.setOnClickListener {
            viewPager.currentItem = 1
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        } else {
            return
        }
    }

    private fun updateDotIndicator(page: Int) {
        when (page) {
            0 -> {
                firstDot.setBackgroundResource(R.drawable.dot_on_color)
                secondDot.setBackgroundResource(R.drawable.dot_on_color)
                secondDot.background.alpha = 128 // 50% opacity
            }
            1 -> {
                firstDot.setBackgroundResource(R.drawable.dot_on_white)
                secondDot.setBackgroundResource(R.drawable.dot_on_white)
                firstDot.background.alpha = 128 // 50% opacity
            }
        }
    }

    private fun setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_onboarding)
    }

    private fun ViewPager.setOnPageListener() {
        this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                updateDotIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

        })
    }
}
