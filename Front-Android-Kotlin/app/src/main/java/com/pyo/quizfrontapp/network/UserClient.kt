package com.pyo.quizfrontapp.network

import com.google.gson.GsonBuilder
import com.pyo.quizfrontapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory




object UserClient {
    var retrofitService: QuizService


    private val BASE_URL : String = BuildConfig.SERVER_URL

        init {
        val gson = GsonBuilder().setLenient().create()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        retrofitService = retrofit.create(QuizService::class.java)


    }
}