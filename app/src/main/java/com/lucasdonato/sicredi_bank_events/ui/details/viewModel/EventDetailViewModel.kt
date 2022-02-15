package com.lucasdonato.sicredi_bank_events.ui.details.viewModel

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.details.CheckIn
import com.lucasdonato.sicredi_bank_events.model.data.model.entities.home.Events
import com.lucasdonato.sicredi_bank_events.ui.base.viewmodel.BaseViewModel
import com.lucasdonato.sicredi_bank_events.model.data.useCase.EventsUseCase
import com.lucasdonato.sicredi_bank_events.utils.livedata.MutableLiveDataResource
import com.lucasdonato.sicredi_bank_events.utils.livedata.Resource
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











