package com.example.top_up_weather.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.top_up_weather.data.model.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from weather")
    fun fetchWeather(): Flow<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: Weather?)

    @Query("DELETE FROM weather")
    suspend fun deleteWeather()
}