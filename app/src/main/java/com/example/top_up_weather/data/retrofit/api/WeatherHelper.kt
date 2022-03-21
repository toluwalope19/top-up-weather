package com.example.top_up_weather.data.retrofit.api

import com.example.top_up_weather.data.model.Weather
import retrofit2.Response

interface WeatherHelper {

    suspend fun getCurrentWeather(id: String) : Response<Weather>
}