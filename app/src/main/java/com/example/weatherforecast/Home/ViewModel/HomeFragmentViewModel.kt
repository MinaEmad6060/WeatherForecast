package com.example.weatherforecast.Home.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.CurrentWeather
import com.example.weatherforecast.Model.InterWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar



class HomeFragmentViewModel(private var repo: InterWeatherRepository): ViewModel() {

    private val TAG = "HomeFragmentViewModel"

    private var _weatherList = MutableLiveData<CurrentWeather>()
    var weatherList: LiveData<CurrentWeather> = _weatherList

    fun getWeatherRemoteVM(lat: Double, lon: Double, key: String, units: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO){
            val weatherItems = repo.getWeatherRemoteRepo(lat,lon,key,units,lang)
            weatherItems.date = getDateAndTime().split(" ")[0]
            weatherItems.time = getDateAndTime().split(" ")[1]
            _weatherList.postValue(weatherItems)
        }
    }




    private fun getDateAndTime(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.YEAR)}"+
                "-" + "${calendar.get(Calendar.MONTH) + 1}"+
                "-" + "${calendar.get(Calendar.DAY_OF_MONTH)}"+
                " " + "${calendar.get(Calendar.HOUR_OF_DAY)}"+
                ":" + "${calendar.get(Calendar.MINUTE)}"+
                ":" + "${calendar.get(Calendar.SECOND)}"
    }
}