package com.example.intellilearnteacherapp

import android.app.Application
import okhttp3.OkHttpClient


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//from medium article

//object ServiceBuilder {
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8000/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    fun<T> buildService(service: Class<T>): T{
//        return retrofit.create(service)
//    }
//}

//from youtube video
class MyApp : Application() {

    companion object {
        private lateinit var instance: MyApp
        fun getInstance() = instance
    }

    private var retrofit: Retrofit? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Set the connection timeout to 60 seconds
            .readTimeout(60, TimeUnit.SECONDS) // Set the read timeout to 60 seconds
            .writeTimeout(60, TimeUnit.SECONDS) // Set the write timeout to 60 seconds
            .build()

        retrofit = Retrofit.Builder()
            // .baseUrl("http://10.0.2.2:8000/") // Emulator
            .baseUrl("http://192.168.100.4:8000/") // Real device
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Assign the custom OkHttp client to Retrofit
            .build()
    }

    fun getApiServices() = retrofit!!.create(APIServices::class.java)
}