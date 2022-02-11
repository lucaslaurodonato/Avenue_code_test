package com.lucasdonato.sicredi_bank_events.ui.onboarding.viewModel

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.ViewModel
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.ui.AppApplication.Companion.context

class OnboardingViewModel : ViewModel() {

    fun getRandomBackground(): Drawable? {
        val background = intArrayOf(
            R.drawable.event_photo,
            R.drawable.event_photo_one,
            R.drawable.event_photo_two,
            R.drawable.event_photo_three,
            R.drawable.event_photo_four,
        )
        return context?.let { getDrawable(it, background.random()) }
    }

}











