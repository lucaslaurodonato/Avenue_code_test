package com.lucasdonato.avenue_code_test.presentation.home.presenter

import Events
import com.lucasdonato.avenue_code_test.presentation.AppApplication
import com.lucasdonato.avenue_code_test.data.useCase.EventsUseCase
import com.lucasdonato.avenue_code_test.mechanism.livedata.MutableLiveDataResource
import com.lucasdonato.avenue_code_test.mechanism.livedata.Resource
import com.lucasdonato.avenue_code_test.presentation.base.presenter.BasePresenter

class HomePresenter(
    private val eventsUseCase: EventsUseCase
) : BasePresenter() {

    val getEventsLiveData = MutableLiveDataResource<List<Events>>()

    fun getEventsList() = runCoroutine {
        getEventsLiveData.postValue(Resource.loading())
        eventsUseCase.getEvents()?.let {
            getEventsLiveData.postValue(Resource.success(it))
        } ?: getEventsLiveData.postValue(Resource.error())
    } onError {
        getEventsLiveData.postValue(
            Resource.error(AppApplication.context?.getString(it.errorCode.stringCode))
        )
    }

}











