package com.saviola44.task.data.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor


object ServiceFactory {
    private const val BASE_URL = "https://api.github.com/"
    private const val HTTP_READ_TIMEOUT = 10000L
    private const val HTTP_CONNECT_TIMEOUT = 6000L

    fun <S> createService(serviceClass: Class<S> ): S {
        return createService(makeOkHttpClient(), serviceClass)
    }

    private fun <S> createService(okHttpClient: OkHttpClient, serviceClass: Class<S>): S {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(serviceClass)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClientBuilder = OkHttpClient().newBuilder()
        httpClientBuilder.addInterceptor(interceptor)
        httpClientBuilder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
        return httpClientBuilder.build()
    }
}