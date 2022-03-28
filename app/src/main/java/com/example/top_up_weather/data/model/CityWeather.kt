package com.example.top_up_weather.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "city_weather")
@Parcelize
data class CityWeather (

    @ColumnInfo(name= "coord")
    val coord: Coord,
    @ColumnInfo(name= "dt")
    val dt: Int,
    @ColumnInfo(name= "id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name= "main")
    val main: Main,
    @ColumnInfo(name= "name")
    val name: String,
    @ColumnInfo(name= "sys")
    val sys: Sys,
    @ColumnInfo(name= "visibility")
    val visibility: Int,
    @ColumnInfo(name= "weather")
    val weather: List<WeatherX>,
    @ColumnInfo(name= "wind")
    val wind: Wind,
    @ColumnInfo(name= "isliked")
    val isLiked: Boolean
): Parcelable