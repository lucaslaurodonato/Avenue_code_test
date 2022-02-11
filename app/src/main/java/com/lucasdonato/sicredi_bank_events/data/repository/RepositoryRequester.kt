package com.lucasdonato.sicredi_bank_events.data.repository

import com.google.gson.Gson
import com.lucasdonato.sicredi_bank_events.data.repository.type.WsError
import okhttp3.ResponseBody
import retrofit2.Response

fun <T : Any> performRequest(response: Response<T>, verifyBody: Boolean = true): Any {
    try {
        return if (response.isSuccessful) {
            if (verifyBody) {
                response.body()?.let {
                    it
                } ?: treatError(response.errorBody())
            } else {
                true
            }
        } else {
            treatError(response.errorBody())
        }
    } catch (e: Exception) {
        when (e) {
            /**
             * Include more cases if needed
             */
            is ErrorException -> throw ErrorException(
                e.errorCode
            )
            else -> throw ErrorException(ErrorCode.GENERIC_ERROR)
        }
    }
}

fun treatError(errorBody: ResponseBody? = null) {
    errorBody?.string()?.let { error ->
        /**
         * Customize error class depending project
         * Each project has it's own error structure, customize method to adapt
         */
        Gson().fromJson(error, WsError::class.java)?.let { wsError ->
            throw ErrorException(ErrorCode.fromString(wsError.error))
        }
    } ?: throw ErrorException(ErrorCode.GENERIC_ERROR)
}