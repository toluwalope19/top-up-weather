package com.example.top_up_weather.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.top_up_weather.data.model.CityWeather
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.data.remote.api.RemoteImpl
import com.example.top_up_weather.repository.WeatherRepository
import com.example.top_up_weather.utils.ApiError
import com.example.top_up_weather.utils.Resource
import com.example.top_up_weather.utils.UIEvent
import com.example.top_up_weather.utils.Utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _getWeather = MutableLiveData<UIEvent<Resource<List<CityWeather>>>>()
    val getWeather : LiveData<UIEvent<Resource<List<CityWeather>>>> = _getWeather.asFlow().asLiveData()

    private val countryQueryString =
        "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"


    init {
        getWeatherList()
    }

    fun getWeatherList() {

        viewModelScope.launch {
            _getWeather.postValue(UIEvent(Resource.Loading(null)))
            try {
                val response = repository.fetchWeather()
                response.onEach{
                    _getWeather.postValue(UIEvent(Resource.Success(it.data!!)))
                }.launchIn(viewModelScope)
            } catch (e: Throwable) {
                // val errorMessage = apiError.extractErrorMessage(e)
                _getWeather.postValue(UIEvent(Resource.Error(e.message!!)))
            }
        }

//        viewModelScope.launch {
//
//            _getWeather.postValue(UIEvent(Resource.loading(null)))
//            try {
//                val response = weatherHelperImpl.getCurrentWeather(countryQueryString)
//                _getWeather.postValue(UIEvent(Resource.success(response.body())))
//            } catch (e: Throwable) {
//                _getWeather.postValue(UIEvent(Resource.error(e, null)))
//            }
//        }
    }

    fun saveWeather(weather: CityWeather){
        viewModelScope.launch {
            repository.saveWeather(weather)
        }
    }
}