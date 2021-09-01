package com.lucasdonato.avenue_code_test.data.remote

import CheckIn
import Events
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {

    @GET("events")
    fun getEvents(): Call<List<Events>>

    @GET("events/{id}")
    fun getEventsById(@Path("id") id: Int): Call<Events>

    @POST("checkin")
    fun postCheckIn(@Body checkin: CheckIn): Call<CheckIn>

}