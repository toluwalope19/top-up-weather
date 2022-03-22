package com.example.top_up_weather.data.local

import android.util.Log
import androidx.room.TypeConverter
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import java.io.IOException
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

        val listType = object : TypeToken<List<CityWeather>>() {

        }.type

        return Gson().fromJson<List<CityWeather>>(listOfString, listType)
    }

    @TypeConverter
    fun saveWeather(string: Weather?): String? {
        return Gson().toJson(string)
    }

    @TypeConverter
    fun getWeather(string: String?): Weather? {
        return Gson().fromJson(
            string,
            object : TypeToken<List<String?>?>() {}.type
        )
    }
}