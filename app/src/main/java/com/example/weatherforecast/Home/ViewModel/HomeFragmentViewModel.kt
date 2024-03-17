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
    private val _weatherList = MutableStateFlow<DataState>(DataState.Loading)
    val weatherList: StateFlow<DataState> = _weatherList

    fun getWeatherRemoteVM(lat: Double, lon: Double, key: String, units: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getWeatherRemoteRepo(lat,lon,key,units,lang)
                .catch {
                    _weatherList.value = DataState.Failure(it)
                }
                .collect{
                    it.date = getDateAndTime().split(" ")[0]
                    it.time = getDateAndTime().split(" ")[1]
                    _weatherList.value = DataState.Success(it)
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


    //    fun convertTimeFormat(){
//    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//    val outputFormat = SimpleDateFormat("h a", Locale.getDefault())
//
//    val time = "03:00:00"
//    val date = inputFormat.parse(time)
//    val formattedTime = outputFormat.format(date)
//
//    println(formattedTime)
//    }

//    fun convertDateFormat() {
//        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
//
//        val dateStr = "16/03/2024"
//        val date = inputFormat.parse(dateStr)
//        val dayOfWeek = outputFormat.format(date)
//
//        println(dayOfWeek)
//    }
}