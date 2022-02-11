package com.lucasdonato.sicredi_bank_events.ui.home.viewmodel

import Events
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdonato.avenue_code_test.presentation.base.presenter.BaseViewModel
import com.lucasdonato.sicredi_bank_events.data.useCase.EventsUseCase
import com.lucasdonato.sicredi_bank_events.mechanism.livedata.MutableLiveDataResource
import com.lucasdonato.sicredi_bank_events.mechanism.livedata.Resource
import com.lucasdonato.sicredi_bank_events.ui.AppApplication
import kotlinx.coroutines.launch

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











