package com.lucasdonato.sicredi_bank_events.model.data.useCase

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.details.CheckIn
import com.lucasdonato.sicredi_bank_events.model.data.repository.EventsRepository
import com.lucasdonato.sicredi_bank_events.model.domain.base.runSuspend
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