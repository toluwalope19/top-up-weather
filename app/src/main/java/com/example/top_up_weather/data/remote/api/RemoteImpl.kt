package com.example.top_up_weather.data.remote.api

import com.example.top_up_weather.data.model.Weather
import retrofit2.Response
import javax.inject.Inject

class RemoteImpl @Inject constructor(private val weatherService: WeatherService) :
    RemoteSource {

    override suspend fun getCurrentWeather(id: String): Response<Weather> {
        return weatherService.getCurrentWeather(id)
    }
}