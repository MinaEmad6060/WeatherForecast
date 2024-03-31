package com.example.weatherforecast.Home.ViewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.Remote.Home.DataStateHomeRemote
import com.example.weatherforecast.Model.Local.Home.DataStateHomeRoom
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Repo.Home.InterHomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HomeFragmentViewModel(private var repo: InterHomeRepo): ViewModel() {

    private val TAG = "HomeFragmentViewModel"

    private val _additionalWeatherList = MutableStateFlow<DataStateHomeRemote>(DataStateHomeRemote.Loading)
    val additionalWeatherList: StateFlow<DataStateHomeRemote> = _additionalWeatherList


    private val _homeWeatherList = MutableStateFlow<DataStateHomeRoom>(DataStateHomeRoom.Loading)
    val homeWeatherList: StateFlow<DataStateHomeRoom> = _homeWeatherList

    fun getAdditionalWeatherRemoteVM(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getAdditionalWeatherRemoteRepo(lat,lon,key,units,lang,cnt)
                .catch {
                    _additionalWeatherList.value = DataStateHomeRemote.Failure(it)
                }
                .collect{
                    it.date = getDateAndTime().split(" ")[0]
                    it.time = getDateAndTime().split(" ")[1]
                    _additionalWeatherList.value = DataStateHomeRemote.Success(it)
                }
        }
    }


    fun getAllHomeWeatherVM(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllHomeWeatherLocalRepo()
                .catch {
                    Log.i("vmRoom", "getAllHomeWeatherVM: fail")
                    _homeWeatherList.value = DataStateHomeRoom.Failure(it)
                }
                .collect{
                    Log.i("vmRoom", "getAllHomeWeatherVM: success")
                    _homeWeatherList.value = DataStateHomeRoom.Success(it)
                }
        }
    }

    suspend fun deleteAllHomeWeatherVM(): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteAllHomeWeatherLocalRepo()
            getAllHomeWeatherVM()
        }.await()
        return res
    }

    suspend fun insertAllHomeWeatherVM(homeWeather: HomeWeather): Long{
        var res :Long=0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.insertAllHomeWeatherLocalRepo(homeWeather)
            getAllHomeWeatherVM()
        }.await()
        return res
    }


    fun getDateAndTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

//    fun isNetworkConnected(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
//    }

}