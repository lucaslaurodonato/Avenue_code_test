package com.lucasdonato.sicredi_bank_events.ui.home.viewmodel

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.home.Events
import com.lucasdonato.sicredi_bank_events.ui.base.viewmodel.BaseViewModel
import com.lucasdonato.sicredi_bank_events.model.data.useCase.EventsUseCase
import com.lucasdonato.sicredi_bank_events.utils.livedata.MutableLiveDataResource
import com.lucasdonato.sicredi_bank_events.utils.livedata.Resource
import com.lucasdonato.sicredi_bank_events.ui.AppApplication

class HomeViewModel(private val eventsUseCase: EventsUseCase) : BaseViewModel() {

    private val _eventList = MutableLiveDataResource<List<Events>>()
    val eventList: MutableLiveDataResource<List<Events>> get() = _eventList

    fun getEventsList() = runCoroutine {
        _eventList.postValue(Resource.loading())
        eventsUseCase.getEvents().let {
            _eventList.postValue(Resource.success(it))
        }
    } onError {
        _eventList.postValue(
            Resource.error(AppApplication.context?.getString(it.errorCode.stringCode))
        )
    }

}











