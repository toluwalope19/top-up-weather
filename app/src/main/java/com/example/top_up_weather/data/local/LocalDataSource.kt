package com.example.top_up_weather.data.local

import com.example.top_up_weather.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun save(weather: Weather?)
    fun getWeather(): Flow<Weather>
   suspend fun deleteWeather()
}