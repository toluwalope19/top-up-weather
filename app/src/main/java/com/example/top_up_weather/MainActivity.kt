package com.example.top_up_weather

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.top_up_weather.data.local.worker.FetchResultWorker
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.notification.Notification
import com.example.top_up_weather.notification.messageExtra
import com.example.top_up_weather.notification.titleExtra
import com.example.top_up_weather.ui.home.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val requiredAlarmPermissions = arrayOf(
        Manifest.permission.SET_ALARM
    )

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }


    private val viewModel: HomeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        alarmPermissionGranted()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        toolbar.setupWithNavController(navController)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun alarmPermissionGranted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SET_ALARM
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Perm check:SET_ALARM", "Permission Denied");
            requestPermissions(requiredAlarmPermissions, 1)
        } else {
            Log.d("Perm check:SET_ALARM", "Permission Exists");
        }
    }


    private fun setUpPeriodicRequest() {
        val citiesQueryString =
            "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"

        val workmanager = WorkManager.getInstance(applicationContext)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicRequest = PeriodicWorkRequest
            .Builder(FetchResultWorker::class.java, 16, TimeUnit.MINUTES)
            .setInputData( workDataOf("API_PARAM" to citiesQueryString))
            .setInitialDelay(5,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workmanager.enqueue(periodicRequest)
        workmanager.getWorkInfoByIdLiveData(periodicRequest.id)
            .observe(this, Observer {
                
            })

    }

    private fun getCityWeatherList(listOfString: String?): List<CityWeather?>? {
        if (listOfString == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<CityWeather>>() {

        }.type

        return Gson().fromJson<List<CityWeather>>(listOfString, listType)
    }


    override fun onStart() {
        super.onStart()
        createNotificationDefaultChannel()
        setUpPeriodicRequest()
    }


    private fun createNotificationDefaultChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = IMPORTANCE_HIGH
            val channel =
                NotificationChannel(
                    Channel_ID_DEFAULT,
                    "Channel_Default",
                    importance
                ).apply {
                    description = "This is default channel"
                }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val Channel_ID_DEFAULT: String = "Channel_ID_DEFAULT"
        const val NOTIFICATION_ID = 1
    }

}