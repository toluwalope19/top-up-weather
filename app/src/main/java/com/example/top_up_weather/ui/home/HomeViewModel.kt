package com.example.top_up_weather.ui.home

import androidx.lifecycle.*
import com.example.top_up_weather.data.model.Weather
import com.example.top_up_weather.repository.WeatherRepository
import com.example.top_up_weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _getWeather =  ConflatedBroadcastChannel<Resource<Weather>>(Resource.Loading())
    val getWeather : LiveData<Resource<Weather>> = _getWeather.asFlow().asLiveData()

    private val countryQueryString =
        "524901,703448,2643743,2332459,184742,2643743,2925533,2950158,1850147,1816670,2968815,5165418,5165664,6111984,2867714,4104031,2352778,2634716,2800866,3117735"


    init {
        getWeatherList()
    }

    fun getWeatherList() {
        repository.fetchWeather()
            .onEach {
                _getWeather.trySend(it).isSuccess
            }.launchIn(viewModelScope)

    }

//    fun addFavourites(item: Weather){
//        viewModelScope.launch {
//            repository.saveFavourite(item)
//        }
//    }


}