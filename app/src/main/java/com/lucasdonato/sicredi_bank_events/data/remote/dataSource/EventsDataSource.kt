package com.lucasdonato.sicredi_bank_events.data.remote.dataSource

import CheckIn
import com.lucasdonato.sicredi_bank_events.data.remote.WebService

class EventsDataSource(private val webService: WebService) {

    fun getEvents() = webService.getEvents()

    fun getEventsById(id: Int) = webService.getEventsById(id)

    fun postCheckIn(checkIn: CheckIn) = webService.postCheckIn(checkIn)

}