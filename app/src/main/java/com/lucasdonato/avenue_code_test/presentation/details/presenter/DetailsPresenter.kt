package com.lucasdonato.avenue_code_test.presentation.details.presenter

import Events
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lucasdonato.avenue_code_test.data.useCase.EventsUseCase
import com.lucasdonato.avenue_code_test.mechanism.livedata.MutableLiveDataResource
import com.lucasdonato.avenue_code_test.mechanism.livedata.Resource
import com.lucasdonato.avenue_code_test.presentation.AppApplication
import com.lucasdonato.avenue_code_test.presentation.base.presenter.BasePresenter

class DetailsPresenter(
    private val eventsUseCase: EventsUseCase
) : BasePresenter() {

    val getEventLiveData = MutableLiveDataResource<Events>()

    fun getEventsList(id: Int) = runCoroutine {
        getEventLiveData.postValue(Resource.loading())
        eventsUseCase.getEventsById(id)?.let {
            getEventLiveData.postValue(Resource.success(it))
        } ?: getEventLiveData.postValue(Resource.error())
    } onError {
        getEventLiveData.postValue(
            Resource.error(AppApplication.context?.getString(it.errorCode.stringCode))
        )
    }


}











