package com.example.top_up_weather.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.top_up_weather.data.local.TypeConverters
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey
    val id: Int = 0,
    val cnt: Int,
    val list: List<CityWeather>
)

@Parcelize
data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
): Parcelable

@Parcelize
data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
): Parcelable

@Parcelize
data class Wind(
    val deg: Int,
    val speed: Double
): Parcelable

@Parcelize
data class Main(
    val feels_like: Double,
    val humidity: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double,
    val pressure: Int
): Parcelable

@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
): Parcelable
