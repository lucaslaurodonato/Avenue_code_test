package com.lucasdonato.avenue_code_test.data.repository.events

import CheckIn
import Events
import com.lucasdonato.avenue_code_test.data.remote.dataSource.EventsDataSource
import com.lucasdonato.avenue_code_test.data.repository.performRequest
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