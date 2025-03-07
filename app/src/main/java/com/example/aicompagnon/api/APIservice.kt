package com.example.aicompagnon.api

import com.example.aicompagnon.models.EventModel
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("events.json")
    fun getEvents(): Call<List<EventModel>>
}