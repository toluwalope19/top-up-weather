package com.example.top_up_weather.data.model

data class Weather(
    val cnt: Int,
    val list: List<CityWeather>
)

data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class Wind(
    val deg: Int,
    val speed: Double
)

data class Main(
    val feels_like: Double,
    val humidity: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class CityWeather (
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: Wind
)