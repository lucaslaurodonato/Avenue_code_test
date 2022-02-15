package com.lucasdonato.sicredi_bank_events.ui.splash.view

import android.os.Bundle
import android.os.Handler
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.databinding.ActivitySplashBinding
import com.lucasdonato.sicredi_bank_events.utils.constants.SPLASH_DISPLAY_TIME
import com.lucasdonato.sicredi_bank_events.ui.base.view.BaseActivity
import com.lucasdonato.sicredi_bank_events.ui.onboarding.view.OnboardingActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            startWelcomeScreen()
        }, SPLASH_DISPLAY_TIME.toLong())
    }

    private fun startWelcomeScreen() {
        startActivity(OnboardingActivity.getStartIntent(this))
        finish()
    }

}