package com.example.top_up_weather.repository

import android.util.Log
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.utils.AppCoroutineDispatchers
import com.example.top_up_weather.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val dispatcher: AppCoroutineDispatchers,
    private val remoteSource: RemoteSource,
    private val localDataSource: LocalDataSource,
) : Repository {

    private val citiesQueryString =
        "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"


    override fun fetchWeather(): Flow<Resource<Weather>> {
        return flow<Resource<Weather>> {
            val currentData: Weather = localDataSource.getWeather().first()
            Log.e("currrentdata", currentData.toString())
            emit(Resource.Loading(currentData))
            fetchWeatherAndCache()
            emitAll(localDataSource.getWeather().map { Resource.Success(it) })
        }.catch { cause ->
            val previousData: Weather = localDataSource.getWeather().first()
            emit(Resource.Error(cause, previousData))
            cause.printStackTrace()
        }.flowOn(dispatcher.io)
    }

    private suspend fun fetchWeatherAndCache() {
        val weather: Weather? = remoteSource.getCurrentWeather(citiesQueryString).body()
        localDataSource.save(weather)
    }



}