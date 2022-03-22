package com.example.top_up_weather.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.data.retrofit.api.WeatherHelperImpl
import com.example.top_up_weather.utils.ApiError
import com.example.top_up_weather.utils.Resource
import com.example.top_up_weather.utils.UIEvent
import com.example.top_up_weather.utils.Utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherHelperImpl: WeatherHelperImpl,
    private val apiError: ApiError
) : ViewModel() {

    private val _getWeather = MutableLiveData<UIEvent<Resource<Weather>>>()
    val getWeather = _getWeather.asLiveData()

    private val countryQueryString =
        "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"

    fun getWeatherList() {
        viewModelScope.launch {

            _getWeather.postValue(UIEvent(Resource.loading(null)))
            try {
                val response = weatherHelperImpl.getCurrentWeather(countryQueryString)
                _getWeather.postValue(UIEvent(Resource.success(response.body())))
            } catch (e: Exception) {
                val errorMessage = apiError.extractErrorMessage(e)
                _getWeather.postValue(UIEvent(Resource.error(errorMessage, null)))
            }
        }
    }
}