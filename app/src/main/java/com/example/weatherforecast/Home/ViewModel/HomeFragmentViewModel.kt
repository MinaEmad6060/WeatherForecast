package com.example.weatherforecast.Home.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.DataState
import com.example.weatherforecast.Model.InterWeatherRepository
import com.example.weatherforecast.Model.Local.Home.DataStateHome
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar



class HomeFragmentViewModel(private var repo: InterWeatherRepository): ViewModel() {

    private val TAG = "HomeFragmentViewModel"

    private val _additionalWeatherList = MutableStateFlow<DataState>(DataState.Loading)
    val additionalWeatherList: StateFlow<DataState> = _additionalWeatherList


    private val _homeWeatherList = MutableStateFlow<DataStateHome>(DataStateHome.Loading)
    val homeWeatherList: StateFlow<DataStateHome> = _homeWeatherList

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


    fun getAllHomeWeatherVM(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllHomeWeatherLocalRepo(context)
                .catch {
                    Log.i("vmRoom", "getAllHomeWeatherVM: fail")
                    _homeWeatherList.value = DataStateHome.Failure(it)
                }
                .collect{
                    Log.i("vmRoom", "getAllHomeWeatherVM: success")
                    _homeWeatherList.value = DataStateHome.Success(it)
                }
        }
    }

    fun deleteAllHomeWeatherVM(context: Context): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteAllHomeWeatherLocalRepo(context)
            getAllHomeWeatherVM(context)
        }
        return res
    }

    suspend fun insertAllHomeWeatherVM(homeWeather: HomeWeather, context: Context): Long{
        var res :Long=0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.insertAllHomeWeatherLocalRepo(homeWeather,context)
            getAllHomeWeatherVM(context)
        }
        return res
    }


    fun getDateAndTime(): String {
        val calendar = Calendar.getInstance()
        return "${calendar.get(Calendar.YEAR)}"+
                "-" + "${calendar.get(Calendar.MONTH) + 1}"+
                "-" + "${calendar.get(Calendar.DAY_OF_MONTH)}"+
                " " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + "${calendar.get(Calendar.MINUTE)}"+
                ":" + String.format("%02d", calendar.get(Calendar.SECOND))
    }

}