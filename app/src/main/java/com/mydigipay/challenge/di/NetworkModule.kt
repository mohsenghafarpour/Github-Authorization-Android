package com.mydigipay.challenge.di

import com.mydigipay.challenge.di.Qualifiers.CONNECTION_TIMEOUT
import com.mydigipay.challenge.di.Qualifiers.LOGGING_INTERCEPTOR
import com.mydigipay.challenge.di.Qualifiers.READ_TIMEOUT
import com.mydigipay.challenge.di.Qualifiers.WRITE_TIMEOUT
import com.mydigipay.challenge.utils.ktx.logD
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single(READ_TIMEOUT) { 30 * 1000L }
    single(WRITE_TIMEOUT) { 10 * 1000L }
    single(CONNECTION_TIMEOUT) { 10 * 1000L }

    single<Interceptor>(LOGGING_INTERCEPTOR) {
        HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    logD(message)
                }
            }
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .readTimeout(get(READ_TIMEOUT), TimeUnit.MILLISECONDS)
            .writeTimeout(get(WRITE_TIMEOUT), TimeUnit.MILLISECONDS)
            .connectTimeout(get(CONNECTION_TIMEOUT), TimeUnit.MILLISECONDS)
            .addInterceptor(get<Interceptor>(LOGGING_INTERCEPTOR))
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}