package com.example.top_up_weather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.top_up_weather.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from city_weather")
    fun fetchWeather(): Flow<List<CityWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: List<CityWeather>?)

    @Query("DELETE FROM city_weather")
    suspend fun deleteWeather()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(weather: CityWeather)

    @Query("select * from city_weather where isliked = :isLiked  ")
    fun fetchFavourites(isLiked: Boolean): List<CityWeather>

    @Query("select * from city_weather where id = :id")
    fun isFavourite(id: Int): Flow<List<CityWeather>>


    @Query("UPDATE city_weather  SET wind = :wind, sys = :sys, main = :main,visibility = :visibility, weather = :weather where id = :id")
    fun update(wind: Wind, sys: Sys, main: Main, visibility: Int, weather: List<WeatherX>, id: Int)


}