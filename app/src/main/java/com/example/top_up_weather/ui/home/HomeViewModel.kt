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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _getWeather = MutableLiveData<UIEvent<Resource<List<CityWeather>>>>()
    val getWeather: LiveData<UIEvent<Resource<List<CityWeather>>>> =
        _getWeather.asFlow().asLiveData()

    val text: MutableLiveData<String> = MutableLiveData()

    init {
        text.value = ""
    }

    fun getWeatherList() {

        viewModelScope.launch {
            _getWeather.postValue(UIEvent(Resource.Loading(null)))
            try {
                val response = repository.fetchWeather()
                response.onEach {
                    _getWeather.postValue(UIEvent(Resource.Success(it.data!!)))
                }.launchIn(viewModelScope)
            } catch (e: Throwable) {
                // val errorMessage = apiError.extractErrorMessage(e)
                _getWeather.postValue(UIEvent(Resource.Error(e.message!!)))
            }
        }
    }


    val weatherCities = text.switchMap {
        liveData(Dispatchers.IO) {
            if (it == null || it == "") {
                val data = repository.fetchWeather().asLiveData()
                emitSource(data)
            } else {
                val data = repository.searchWeatherCities(it)
                emitSource(data)
            }
        }
    }


    fun search(searchText: String) {
        text.value = searchText
    }

    fun saveWeather(weather: CityWeather) {
        viewModelScope.launch {
            repository.saveWeather(weather)
        }
    }
}