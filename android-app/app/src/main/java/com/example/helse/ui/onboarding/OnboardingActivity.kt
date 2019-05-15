package com.example.helse.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.viewpager.widget.ViewPager
import com.example.helse.MainActivity
import com.example.helse.R
import com.example.helse.adapters.OnboardingAdapter
import com.example.helse.ui.settings.DashboardSettingsFragment
import com.example.helse.ui.settings.LocationSettingsFragment
import com.example.helse.ui.settings.PushSettingsFragment
import com.example.helse.utilities.Injector
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setFullscreen()
        super.onCreate(savedInstanceState)
        viewPager.adapter = OnboardingAdapter(supportFragmentManager)
        viewPager.setOnPageListener()

        updateDotIndicator(0)
        setDotClickListener()

        finishBtn.setOnClickListener {
            // if location == null, toast: må velge posisjon
            finishOnboarding()
        }

        nextBtn.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem + 1
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    private fun finishOnboarding() {
        val preference = Injector.getAppPreferences(this)
        preference.setFirstLaunch(false)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        } else {
            return
        }
    }

    private fun updateDotIndicator(page: Int) {
        if (page == 0) {
            firstDot.setBackgroundResource(R.drawable.dot_on_color)
            secondDot.setBackgroundResource(R.drawable.dot_on_color)
            thirdDot.setBackgroundResource(R.drawable.dot_on_color)
            fourthDot.setBackgroundResource(R.drawable.dot_on_color)
        } else {
            firstDot.setBackgroundResource(R.drawable.dot_on_white)
            secondDot.setBackgroundResource(R.drawable.dot_on_white)
            thirdDot.setBackgroundResource(R.drawable.dot_on_white)
            fourthDot.setBackgroundResource(R.drawable.dot_on_white)
        }
        // Set all dots to 50% opacity
        firstDot.background.alpha = 128
        secondDot.background.alpha = 128
        thirdDot.background.alpha = 128
        fourthDot.background.alpha = 128

        // Set current dot fully opaque
        when (page) {
            0 -> {
                firstDot.background.alpha = 255
            }
            1 -> {
                secondDot.background.alpha = 255
            }
            2 -> {
                thirdDot.background.alpha = 255
            }
            3 -> {
                fourthDot.background.alpha = 255
            }
        }
    }

    private fun setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_onboarding)
    }

    private fun setDotClickListener() {
        firstDot.setOnClickListener {
            viewPager.currentItem = 0
        }
        secondDot.setOnClickListener {
            viewPager.currentItem = 1
        }
        thirdDot.setOnClickListener {
            viewPager.currentItem = 2
        }
        fourthDot.setOnClickListener {
            viewPager.currentItem = 3
        }
    }

    private fun ViewPager.setOnPageListener() {
        this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                updateDotIndicator(position)
                if (position == 1 || position == 2) {
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.VISIBLE
                    finishBtn.visibility = View.GONE
                } else if (position == 3) {
                    nextBtn.visibility = View.GONE
                    finishBtn.visibility = View.VISIBLE
                } else {
                    finishBtn.visibility = View.GONE
                    nextBtn.visibility = View.GONE
                    backBtn.visibility = View.GONE
                }
            }


            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

        })
    }
}
