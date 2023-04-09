package com.example.intellilearnteacherapp

import android.app.Application


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    companion object{

        private lateinit var instance : MyApp
        fun getInstance() = instance

    }

    private var retrofit : Retrofit? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun getApiServices() = retrofit!!.create(APIServices::class.java)

}