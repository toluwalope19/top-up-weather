package com.example.top_up_weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.top_up_weather.data.local.worker.FetchResultWorker
import com.example.top_up_weather.ui.home.HomeViewModel
import com.example.top_up_weather.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    }


    private fun setUpPeriodicRequest() {
        val workmanager = WorkManager.getInstance(applicationContext)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicRequest = PeriodicWorkRequest
            .Builder(FetchResultWorker::class.java,16,TimeUnit.MINUTES)
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

//    override fun onPause() {
//        super.onPause()
//        setUpPeriodicRequest()
//    }

    override fun onStart() {
        super.onStart()
        setUpPeriodicRequest()
    }

}