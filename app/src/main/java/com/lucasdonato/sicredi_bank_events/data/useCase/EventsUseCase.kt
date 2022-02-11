package com.lucasdonato.sicredi_bank_events.data.useCase

import CheckIn
import com.lucasdonato.sicredi_bank_events.data.repository.events.EventsRepository
import com.lucasdonato.sicredi_bank_events.domain.base.runSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventsUseCase(private val eventsRepository: EventsRepository) {

    suspend fun getEvents() = withContext(Dispatchers.IO) {
        runSuspend {
            eventsRepository.getEvents()
        }
    }

    suspend fun getEventsById(id: Int) = withContext(Dispatchers.IO) {
        runSuspend {
            eventsRepository.getEventsById(id)
        }
    }

    suspend fun postCheckIn(checkIn: CheckIn) = withContext(Dispatchers.IO) {
        runSuspend {
            eventsRepository.postCheckIn(checkIn)
        }
    }

}