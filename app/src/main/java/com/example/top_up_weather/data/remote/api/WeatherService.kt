package com.example.top_up_weather.data.remote.api

import com.example.top_up_weather.BuildConfig
import com.example.top_up_weather.data.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("group")
    suspend fun getCurrentWeather(
        @Query("id") id: String,
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): Response<Weather>
    
}