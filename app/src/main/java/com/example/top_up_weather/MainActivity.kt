package com.example.top_up_weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.top_up_weather.ui.HomeViewModel
import com.example.top_up_weather.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.getWeatherList()
        setUpObserver()
    }


    private fun setUpObserver(){
        viewModel.getWeather.observe(this, Observer { uiEvent ->
            uiEvent.getContentIfNotHandled()?.let {
                when(it.status){
                    Resource.Status.LOADING -> {
                        progress.visibility = View.VISIBLE
                    }
                    Resource.Status.SUCCESS -> {
                        progress.visibility = View.GONE
                        textView.text = it.data?.list.toString()
                        Log.d("response", it.data?.list.toString())
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        })
    }
}