package com.example.top_up_weather.data.remote.api

import com.example.top_up_weather.data.model.Weather
import retrofit2.Response

interface RemoteSource {

    suspend fun getCurrentWeather(id: String) : Response<Weather>
}