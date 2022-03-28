package com.example.top_up_weather.repository

import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun fetchWeather(): Flow<Resource<List<CityWeather>>>
}