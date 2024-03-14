package com.example.weatherforecast.Home.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.InterWeatherRepository
import com.example.weatherforecast.Model.Weather
import com.example.weatherforecast.Model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private var repo: InterWeatherRepository): ViewModel() {


    private var _weatherList = MutableLiveData<List<Weather>>()
    var weatherList: LiveData<List<Weather>> = _weatherList

    fun getRetroWeather() {
        viewModelScope.launch(Dispatchers.IO){
            val weatherItems = repo.getAllWeatherRemoteRepo()
            _weatherList.postValue(weatherItems.weather)
        }
    }
}