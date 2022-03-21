package com.example.top_up_weather.data.retrofit.api

import com.example.top_up_weather.data.model.Weather
import retrofit2.Response
import javax.inject.Inject

class WeatherHelperImpl @Inject constructor(private val weatherService: WeatherService) :
    WeatherHelper {

    override suspend fun getCurrentWeather(id: String): Response<Weather> {
        return weatherService.getCurrentWeather(id)
    }
}