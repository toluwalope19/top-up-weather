package com.example.top_up_weather.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.top_up_weather.R
import com.example.top_up_weather.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.weather_detail_fragment.*


@AndroidEntryPoint
class WeatherDetailFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherDetailFragment()
    }

    private val viewModel: WeatherDetailViewModel by viewModels()

    private val args by navArgs<WeatherDetailFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_detail_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }



    private fun setView(){
        city.text = args.cityWeather.name
        temp.text = args.cityWeather.main.temp.toString()
        condition.text = args.cityWeather.weather.first().description
        v_wind.text = args.cityWeather.wind.deg.toString()
        v_humidity.text = args.cityWeather.main.humidity.toString()
        v_visibility.text = args.cityWeather.visibility.toString()
        v_pressure.text = args.cityWeather.main.pressure.toString()
        tv_dewPoint.text = args.cityWeather.main.temp_max.toString()
    }

}