package com.lucasdonato.sicredi_bank_events.ui.details.viewModel

import CheckIn
import Events
import com.lucasdonato.avenue_code_test.presentation.base.presenter.BaseViewModel
import com.lucasdonato.sicredi_bank_events.data.useCase.EventsUseCase
import com.lucasdonato.sicredi_bank_events.mechanism.livedata.MutableLiveDataResource
import com.lucasdonato.sicredi_bank_events.mechanism.livedata.Resource
import com.lucasdonato.sicredi_bank_events.ui.AppApplication

class EventDetailViewModel(
    private val eventsUseCase: EventsUseCase
) : BaseViewModel() {

    private val _getEvent = MutableLiveDataResource<Events>()
    val getEvent: MutableLiveDataResource<Events> get() = _getEvent

    private val _checkIn = MutableLiveDataResource<CheckIn>()
    val checkIn: MutableLiveDataResource<CheckIn> get() = _checkIn

    fun getEventById(id: Int) = runCoroutine {
        _getEvent.postValue(Resource.loading())
        eventsUseCase.getEventsById(id)?.let {
            _getEvent.postValue(Resource.success(it))
        } ?: _getEvent.postValue(Resource.error())
    } onError {
        _getEvent.postValue(
            Resource.error(AppApplication.context?.getString(it.errorCode.stringCode))
        )
    }

    fun postCheckIn(checkIn: CheckIn) = runCoroutine {
        _checkIn.postValue(Resource.loading())
        eventsUseCase.postCheckIn(checkIn)?.let {
            _checkIn.postValue(Resource.success())
        } ?: _checkIn.postValue(Resource.error())
    } onError {
        _checkIn.postValue(
            Resource.error(AppApplication.context?.getString(it.errorCode.stringCode))
        )
    }

}











