package com.example.muamalatgo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.X.X:8080/") // replace with your laptop IP
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
