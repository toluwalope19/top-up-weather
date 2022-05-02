package com.example.top_up_weather.data.local.worker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.top_up_weather.App
import com.example.top_up_weather.MainActivity
import com.example.top_up_weather.R
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.notification.*
import com.example.top_up_weather.repository.WeatherRepository
import com.example.top_up_weather.utils.Resource
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.*

@HiltWorker
class FetchResultWorker @AssistedInject constructor(
    val remoteSource: RemoteSource,
    val localDataSource: LocalDataSource,
    private val repository: WeatherRepository,
    private val sharedPreferences: SharedPreferences,
    @Assisted val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {


    var favourite: CityWeather? = null

    override suspend fun doWork(): Result {

        val apiInput = inputData.getString("API_PARAM") ?: return Result.failure()
        val response = remoteSource.getCurrentWeather(apiInput).body()?.list
        val response1 = localDataSource.getWeather().first()
        val randomFavouriteWeather: CityWeather = response1.first { it.isLiked }
        favourite = response?.filter { it.id == randomFavouriteWeather.id }?.firstOrNull()
        val output = workDataOf("API_PARAM" to favourite.toString())

        if (favourite != null) {
            createNotification()
        }
        return Result.success()
    }


    private fun createNotification() {
        val notification = NotificationCompat.Builder(context, Channel_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("${favourite?.name} Weather Report")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${favourite?.main?.temp} degrees and feels like we have ${favourite?.weather?.first()?.description} ")
            )
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val Channel_ID_DEFAULT: String = "Channel_ID_DEFAULT"
        const val NOTIFICATION_ID = 1
    }

}
