package com.lucasdonato.sicredi_bank_events.model.data.repository

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.details.CheckIn
import com.lucasdonato.sicredi_bank_events.model.data.dataSource.EventsDataSource
import com.lucasdonato.sicredi_bank_events.model.data.model.entities.home.Events
import com.lucasdonato.sicredi_bank_events.model.requester.performRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventsRepository(private val eventsDataSource: EventsDataSource) {

    suspend fun getEvents() = withContext(Dispatchers.IO) {
        (performRequest(
            eventsDataSource.getEvents().execute(), true
        ) as List<Events>)
    }

    suspend fun getEventsById(id: Int) = withContext(Dispatchers.IO) {
        (performRequest(
            eventsDataSource.getEventsById(id).execute(), true
        ) as Events?)
    }

    suspend fun postCheckIn(checkIn: CheckIn) = withContext(Dispatchers.IO) {
        (performRequest(
            eventsDataSource.postCheckIn(checkIn).execute()
        ) as CheckIn?)
    }

}