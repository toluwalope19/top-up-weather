package com.example.top_up_weather.data.local.worker

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.repository.WeatherRepository
import com.example.top_up_weather.utils.Resource
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class FetchResultWorker @AssistedInject constructor(
    val remoteSource: RemoteSource,
    private val repository: WeatherRepository,
    private val sharedPreferences: SharedPreferences,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val citiesQueryString =
        "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"


    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val response = repository.fetchWeather().first()
            val json = saveWeatherList(response.data)
            sharedPreferences.edit().putString("key", json).apply()
        }
        return Result.success()
    }

    private fun saveWeatherList(listOfString: List<CityWeather?>?): String? {
        val list = Gson().toJson(listOfString)
        return list
    }

}