package com.lucasdonato.sicredi_bank_events.data.repository
import com.lucasdonato.sicredi_bank_events.R

enum class ErrorCode(val value: String, val stringCode: Int) {
    GENERIC_ERROR("Generic Error", R.string.error_generic),
    OVER_RATE_LIMIT("OVER_RATE_LIMIT", R.string.over_rate_limit),
    INVALID("Invalid", R.string.error_generic);

    companion object {
        fun fromString(value: String?) = values().find { it.value == value } ?: INVALID
    }
}
