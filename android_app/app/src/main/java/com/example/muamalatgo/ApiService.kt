package com.example.muamalatgo

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun hello(): Response<String>
}
