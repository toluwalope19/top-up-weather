package com.example.top_up_weather.data.local

import com.example.top_up_weather.data.model.*
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveList(weather: List<CityWeather>?)
    fun getWeather(): Flow<List<CityWeather>>
    suspend fun deleteWeather()
    suspend fun saveWeather(weather: CityWeather)
    fun getFavorites(isLiked: Boolean): List<CityWeather>
    fun isFavorite(id: Int): Flow<List<CityWeather>>
    fun update(wind: Wind, sys: Sys, main: Main, visibility: Int, weather: List<WeatherX>,id: Int)
    // suspend fun save(weather: CityWeather)
}