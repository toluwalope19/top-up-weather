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
import com.example.top_up_weather.utils.WeatherIconUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.weather_detail_fragment.*
import java.text.SimpleDateFormat
import java.util.*


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
         WeatherIconUtils.getIconResource(requireContext(),weatherIcon,args.cityWeather.weather.first().description)
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy/MM/dd ")
        location.text =  "${args.cityWeather.name}, ${args.cityWeather.sys.country}"
        temp.text = args.cityWeather.main.temp.toString()+ " \u2109"
        condition.text = args.cityWeather.weather.first().description
        v_wind.text = args.cityWeather.wind.deg.toString()
        v_humidity.text = args.cityWeather.main.humidity.toString() +"%"
        v_low_temp.text = args.cityWeather.main.temp_min.toString() + " \u2109"
        date_weather.text = dateInString
    }


    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}