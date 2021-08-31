package com.lucasdonato.avenue_code_test.data.remote.dataSource

import com.lucasdonato.avenue_code_test.data.remote.WebService

class EventsDataSource(private val webService: WebService) {

    fun getEvents() = webService.getEvents()

    fun getEventsById(id: Int) = webService.getEventsById(id)

}