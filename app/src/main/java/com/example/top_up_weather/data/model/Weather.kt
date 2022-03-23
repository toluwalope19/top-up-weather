package com.example.top_up_weather.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass


@Entity(tableName = "weather")
@JsonClass(generateAdapter = true)
data class Weather(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "cnt")
    val cnt: Int,
    @ColumnInfo(name = "list")
    val list: List<CityWeather>
)

@JsonClass(generateAdapter = true)
data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

@JsonClass(generateAdapter = true)
data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

@JsonClass(generateAdapter = true)
data class Wind(
    val deg: Int,
    val speed: Double
)

@JsonClass(generateAdapter = true)
data class Main(
    val feels_like: Double,
    val humidity: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

@JsonClass(generateAdapter = true)
data class Coord(
    val lat: Double,
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class CityWeather (
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: Wind,
)