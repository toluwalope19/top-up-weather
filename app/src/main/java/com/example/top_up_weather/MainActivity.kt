package com.example.top_up_weather

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.top_up_weather.data.local.worker.FetchResultWorker
import com.example.top_up_weather.data.model.CityWeather
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

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }


    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        toolbar.setupWithNavController(navController)

        val workerData = getPeriodicData()

        val listOfFavourites = workerData?.filter {
            it?.isLiked == true
        }
        Log.e("mysavedshared", listOfFavourites.toString())
    }


    private fun getPeriodicData(): List<CityWeather?>? {
        val response = sharedPreferences.getString("key", "")
        return getCityWeatherList(response)
    }

    private fun setUpPeriodicRequest() {
        val workmanager = WorkManager.getInstance(applicationContext)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicRequest = PeriodicWorkRequest
            .Builder(FetchResultWorker::class.java, 16, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workmanager.enqueue(periodicRequest)
        workmanager.getWorkInfoByIdLiveData(periodicRequest.id)
            .observe(this, Observer {
                //val r = it.outputData.hasKeyWithValueOfType("result")
                val r = it.outputData.getString("result")

                Log.e("outputdatas", r.toString())
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
        setUpPeriodicRequest()
    }

}