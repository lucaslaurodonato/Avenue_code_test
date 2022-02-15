package com.lucasdonato.sicredi_bank_events.model.data.model.entities

import java.io.Serializable

data class Address(
    var fullAddress: String = "",
    var neighborhood: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = "Brasil",
    var postalCode: String? = null,
    var complement: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
) : Serializable

fun Address.toAddressString(): String {
    var string = ""
    neighborhood?.let {
        string += it
    }

    city?.let {
        string += ", $it"
    }

    state?.let {
        string += " - $it"
    }

    postalCode?.let {
        string += ", $it"
    }

    country?.let {
        string += ", $it"
    }

    return string
}
