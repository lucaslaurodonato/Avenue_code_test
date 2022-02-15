package com.lucasdonato.sicredi_bank_events.model.data.dataSource

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.details.CheckIn
import com.lucasdonato.sicredi_bank_events.model.data.remote.WebService

class EventsDataSource(private val webService: WebService) {

    fun getEvents() = webService.getEvents()

    fun getEventsById(id: Int) = webService.getEventsById(id)

    fun postCheckIn(checkIn: CheckIn) = webService.postCheckIn(checkIn)

}