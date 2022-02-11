package com.lucasdonato.sicredi_bank_events.mechanism.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

data class Resource<out DataType>(
    val status: Status, val message: String? = null,
    val errorCode: Int? = null, val data: DataType? = null, val errorMessageId: Int? = null
) {

    companion object {

        fun <DataType> loading(data: DataType? = null): Resource<DataType> = Resource(
            Status.LOADING, data = data
        )

        fun <DataType> success(data: DataType? = null): Resource<DataType> = Resource(
            Status.SUCCESS, data = data
        )

        fun <DataType> success(
            data: DataType? = null,
            status: Status
        ): Resource<DataType> = Resource(status, data = data)

        fun <DataType> error(
            message: String? = null, errorCode: Int? = null,
            errorMessageId: Int? = null, data: DataType? = null
        ): Resource<DataType> = Resource(
            Status.ERROR,
            message = message, errorCode = errorCode, errorMessageId = errorMessageId, data = data
        )
    }

}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

typealias LiveDataResource<DataType> = LiveData<Resource<DataType>>

typealias MutableLiveDataResource<DataType> = MutableLiveData<Resource<DataType>>

typealias MediatorLiveDataResource<DataType> = MediatorLiveData<Resource<DataType>>

typealias ObserverResource<DataType> = Observer<Resource<DataType>>