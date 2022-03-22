package com.example.top_up_weather.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.top_up_weather.R
import com.example.top_up_weather.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherDetailFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherDetailFragment()
    }

    private val viewModel: WeatherDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_detail_fragment, container, false)
    }


}