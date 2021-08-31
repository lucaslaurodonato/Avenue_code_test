package com.lucasdonato.avenue_code_test.data.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lucasdonato.avenue_code_test.BuildConfig
import com.lucasdonato.avenue_code_test.mechanism.constants.APPLICATION_JSON
import com.lucasdonato.avenue_code_test.mechanism.constants.CONTENT_TYPE
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServiceClient {

    var webService: WebService

    init {
        webService =
            createDefaultWebService(BuildConfig.BASE_URL + BuildConfig.API)
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val TIMEOUT = 30000L
    }

    private fun createDefaultWebService(
        uri: String, gson: Gson = getGson(),
        timeout: Long = TIMEOUT
    ) = createRetrofitAccess(uri, timeout, Interceptor { chain ->

        var request = chain.request()
        val originalHttpUrl = request.url()
        val url = originalHttpUrl.newBuilder().build()

        val builder = request.newBuilder()
        builder.addHeader(CONTENT_TYPE, APPLICATION_JSON)
        builder.url(url)

        request = builder.build()
        chain.proceed(request)
    }, gson).create(WebService::class.java)

    private fun createRetrofitAccess(
        uri: String, timeout: Long, requestInterceptor: Interceptor,
        gson: Gson = getGson()
    ) = Retrofit.Builder().baseUrl(uri).client(
        setupInterceptors(requestInterceptor, timeout).build()
    ).addConverterFactory(
        GsonConverterFactory.create(gson)
    ).build()

    private fun setupInterceptors(
        requestInterceptor: Interceptor,
        timeout: Long
    ) = OkHttpClient.Builder().apply {

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS
            else HttpLoggingInterceptor.Level.NONE

        addInterceptor(loggingInterceptor)
        addInterceptor(requestInterceptor)
        addNetworkInterceptor(StethoInterceptor())
        connectTimeout(timeout, TimeUnit.SECONDS)
        readTimeout(timeout, TimeUnit.SECONDS)
    }

    private fun getGson() = GsonBuilder().setDateFormat(DATE_FORMAT).create()

}