package com.lucasdonato.sicredi_bank_events.model.data.model.entities.details

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckIn(
    @SerializedName("eventId") val eventId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
) : Serializable