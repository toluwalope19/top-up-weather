package com.example.top_up_weather.data.local

import com.example.top_up_weather.data.local.db.WeatherDao
import com.example.top_up_weather.data.model.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val weatherDao: WeatherDao)
    : LocalDataSource {

    override suspend fun save(weather: Weather?) {
        weatherDao.insert(weather)
    }

    override fun getWeather(): Flow<Weather> {
        return weatherDao.fetchWeather()
    }

    override suspend fun deleteWeather() {
        weatherDao.deleteWeather()
    }

}