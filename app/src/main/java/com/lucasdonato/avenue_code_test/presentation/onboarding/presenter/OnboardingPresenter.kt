package com.lucasdonato.avenue_code_test.presentation.onboarding.presenter

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.data.useCase.EventsUseCase
import com.lucasdonato.avenue_code_test.mechanism.livedata.MutableLiveDataResource
import com.lucasdonato.avenue_code_test.mechanism.livedata.Resource
import com.lucasdonato.avenue_code_test.presentation.base.presenter.BasePresenter

class OnboardingPresenter(
    private val context: Context
) : BasePresenter() {

    val background = intArrayOf(
        R.drawable.event_photo,
        R.drawable.event_photo_one,
        R.drawable.event_photo_two,
        R.drawable.event_photo_three,
        R.drawable.event_photo_four,
    )

    fun getRandomBackground(): Drawable? {
        return getDrawable(context, background.random())
    }

}











