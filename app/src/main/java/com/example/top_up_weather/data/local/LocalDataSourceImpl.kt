package com.example.top_up_weather.data.local

import androidx.lifecycle.LiveData
import com.example.top_up_weather.data.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val weatherDao: WeatherDao)
    : LocalDataSource {

    override suspend fun saveList(weather: List<CityWeather>?) {
        weatherDao.insert(weather)
    }

    override fun getWeather(): Flow<List<CityWeather>> {
        return weatherDao.fetchWeather()
    }

    override suspend fun deleteWeather() {
        weatherDao.deleteWeather()
    }

    override suspend fun saveWeather(weather: CityWeather) {
        weatherDao.saveWeather(weather)
    }

    override fun getFavorites(isLiked: Boolean): List<CityWeather> {
        return weatherDao.fetchFavourites(isLiked)
    }

    override fun isFavorite(id: Int): Flow<List<CityWeather>> {
        return weatherDao.isFavourite(id)
    }

    override fun update(
        wind: Wind,
        sys: Sys,
        main: Main,
        visibility: Int,
        weather: List<WeatherX>,
        id: Int
    ) {
        return weatherDao.update(wind, sys, main, visibility, weather,id)
    }

    override fun getSearchResult(search:String): LiveData<List<CityWeather>> {
        return weatherDao.getSearchResult(search)
    }

}