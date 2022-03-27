package com.example.top_up_weather.data.local.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.lang.Exception

@HiltWorker
class FetchResultWorker @AssistedInject constructor(
    val remoteSource: RemoteSource,
    val localDataSource: LocalDataSource,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val citiesQueryString =
        "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"


    override suspend fun doWork(): Result {
        var outputData: Data?
            val response = remoteSource.getCurrentWeather(citiesQueryString).body()
            Log.e("newres", response.toString())
            response?.list?.map {
                localDataSource.update(it.wind, it.sys, it.main, it.visibility, it.weather, it.id)
            }
        outputData = Data.Builder().putAll(workDataOf("result" to response?.list.toString())).build()
            Log.e("outputdata", outputData.toString())

        return Result.success(outputData!!)
    }
}