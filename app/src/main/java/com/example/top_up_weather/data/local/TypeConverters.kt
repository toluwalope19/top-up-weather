package com.example.top_up_weather.data.local

import android.util.Log
import androidx.room.TypeConverter
import com.example.top_up_weather.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class TypeConverters {


    @TypeConverter
    fun saveWeatherList(listOfString: List<CityWeather?>?): String? {
        val list = Gson().toJson(listOfString)
        return list
    }

    @TypeConverter
    fun getCityWeatherList(listOfString: String?): List<CityWeather?>? {
        if (listOfString == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Weather>>() {

        }.type

        return Gson().fromJson<List<CityWeather>>(listOfString, listType)
    }


    @TypeConverter
    fun saveCoord(string: Coord?): String? {
        return Gson().toJson(string)
    }

    @TypeConverter
    fun saveMain(string: Main?): String? {
        return Gson().toJson(string)
    }

    @TypeConverter
    fun getCoord(string: String?): Coord? {
        return Gson().fromJson(
            string,
            object : TypeToken<Coord?>() {}.type
        )
    }

    @TypeConverter
    fun getMain(string: String?): Main? {
        return Gson().fromJson(
            string,
            object : TypeToken<Main?>() {}.type
        )
    }

    @TypeConverter
    fun saveSys(string: Sys?): String? {
        return Gson().toJson(string)
    }

    @TypeConverter
    fun getSys(string: String?): Sys? {
        return Gson().fromJson(
            string,
            object : TypeToken<Sys?>() {}.type
        )
    }

    @TypeConverter
    fun saveWeatherX(listOfString: List<WeatherX?>?): String? {
        val list = Gson().toJson(listOfString)
        return list
    }

    @TypeConverter
    fun getWeatherX(listOfString: String?): List<WeatherX?>? {
        if (listOfString == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<WeatherX>>() {

        }.type

        return Gson().fromJson<List<WeatherX>>(listOfString, listType)
    }

    @TypeConverter
    fun getWind(string: String?): Wind? {
        return Gson().fromJson(
            string,
            object : TypeToken<Wind?>() {}.type
        )
    }

    @TypeConverter
    fun saveWind(string: Wind?): String? {
        return Gson().toJson(string)
    }

}