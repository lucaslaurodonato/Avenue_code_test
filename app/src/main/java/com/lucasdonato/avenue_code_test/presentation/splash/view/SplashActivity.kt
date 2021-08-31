package com.lucasdonato.avenue_code_test.presentation.splash.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.mechanism.constants.SPLASH_DISPLAY_TIME
import com.lucasdonato.avenue_code_test.presentation.onboarding.view.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startWelcomeScreen()
        }, SPLASH_DISPLAY_TIME.toLong())
    }

    private fun startWelcomeScreen() {
        startActivity(OnboardingActivity.getStartIntent(this))
        finish()
    }

}