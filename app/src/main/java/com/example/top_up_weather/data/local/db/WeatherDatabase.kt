package com.example.top_up_weather.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.top_up_weather.data.local.TypeConverters
import com.example.top_up_weather.data.model.Weather

@Database(entities = [Weather::class], version = 1,exportSchema = false)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object{
        val DATABASE_NAME: String = "weather_db"
    }
}