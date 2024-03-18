package com.example.weatherforecast.Home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.DataState
import com.example.weatherforecast.Model.InterWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar



class HomeFragmentViewModel(private var repo: InterWeatherRepository): ViewModel() {

    private val TAG = "HomeFragmentViewModel"

    private val _additionalWeatherList = MutableStateFlow<DataState>(DataState.Loading)
    val additionalWeatherList: StateFlow<DataState> = _additionalWeatherList

    fun getAdditionalWeatherRemoteVM(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getAdditionalWeatherRemoteRepo(lat,lon,key,units,lang,cnt)
                .catch {
                    _additionalWeatherList.value = DataState.Failure(it)
                }
                .collect{
                    it.date = getDateAndTime().split(" ")[0]
                    it.time = getDateAndTime().split(" ")[1]
                    _additionalWeatherList.value = DataState.Success(it)
                }
        }
    }


    private fun getDateAndTime(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.YEAR)}"+
                "-" + "${calendar.get(Calendar.MONTH) + 1}"+
                "-" + "${calendar.get(Calendar.DAY_OF_MONTH)}"+
                " " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + "${calendar.get(Calendar.MINUTE)}"+
                ":" + String.format("%02d", calendar.get(Calendar.SECOND))
    }

}