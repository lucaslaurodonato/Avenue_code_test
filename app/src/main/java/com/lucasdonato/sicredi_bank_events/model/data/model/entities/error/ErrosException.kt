package com.lucasdonato.sicredi_bank_events.model.data.model.entities.error

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.error.ErrorCode
import java.lang.RuntimeException

class ErrorException(var errorCode: ErrorCode, var errorMessage: String? = "") : RuntimeException()


