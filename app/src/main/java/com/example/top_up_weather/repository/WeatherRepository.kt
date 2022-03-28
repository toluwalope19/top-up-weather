package com.example.top_up_weather.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.top_up_weather.AppCoroutineDispatcher
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val dispatcher: AppCoroutineDispatcher,
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

            if (!currentData.isNullOrEmpty()) {
                fetchWeatherAndCache()
            } else {
                val weather = remoteSource.getCurrentWeather(citiesQueryString).body()?.list
                localDataSource.saveList(weather)
            }

            emitAll(localDataSource.getWeather().map { Resource.Success(it) })
        }.catch { cause ->
            val previousData: List<CityWeather> = localDataSource.getWeather().first()
            emit(Resource.Error(cause.message!!, previousData))
            cause.printStackTrace()
        }.flowOn(dispatcher.io)
    }

    fun searchWeatherCities(text:String) = resultLiveDataDatabase(
        databaseQuery = {
            localDataSource.getSearchResult(text)
        }
    )


    private suspend fun fetchWeatherAndCache() {
        val weather = remoteSource.getCurrentWeather(citiesQueryString).body()?.list
        weather?.map {
            localDataSource.update(it.wind, it.sys, it.main, it.visibility, it.weather, it.id)
        }
    }

    suspend fun saveWeather(weather: CityWeather) {
        localDataSource.saveWeather(weather)
    }

   private fun <T> resultLiveDataDatabase(databaseQuery: () -> LiveData<T>): LiveData<Resource<T>> =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading<T>())
            val source = databaseQuery.invoke().map { Resource.Success(it) }
            emitSource(source)
        }


}