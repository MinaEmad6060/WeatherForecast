package com.example.weatherforecast.Alert.ViewModel


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.Repo.InterWeatherRepository
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Alert.DataStateAlertRoom
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertFragmentViewModel(private var repo: InterWeatherRepository): ViewModel() {
    private val _alertWeatherRoom= MutableStateFlow<DataStateAlertRoom>(DataStateAlertRoom.Loading)
    val alertWeatherRoom: StateFlow<DataStateAlertRoom> = _alertWeatherRoom

    private val _alertWeatherRemote= MutableStateFlow<DataStateAlertRemote>(DataStateAlertRemote.Loading)
    val alertWeatherRemote: StateFlow<DataStateAlertRemote> = _alertWeatherRemote


    fun getAlertWeatherRemoteVM(lat: Double, lon: Double, key: String) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getAlertWeatherRemoteRepo(lat,lon,key)
                .catch {
                    _alertWeatherRemote.value = DataStateAlertRemote.Failure(it)
                }
                .collect{
                    _alertWeatherRemote.value = DataStateAlertRemote.Success(it)
                }
        }
    }

    fun getAlertWeatherVM(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAlertWeatherLocalRepo(context)
                .catch {
                    Log.i("vmRoom", "getAllHomeWeatherVM: fail")
                    _alertWeatherRoom.value = DataStateAlertRoom.Failure(it)
                }
                .collect{
                    Log.i("vmRoom", "getAllHomeWeatherVM: success")
                    _alertWeatherRoom.value = DataStateAlertRoom.Success(it)
                }
        }
    }

//    fun deleteAlertWeatherVM(id: Int, context: Context): Int{
//        var res =0
//        viewModelScope.async(Dispatchers.IO) {
//            res = repo.deleteAlertWeatherLocalRepo(id,context)
//            getAlertWeatherVM(context)
//        }
//        return res
//    }
    fun deleteAlertWeatherVM(id: String, context: Context): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteAlertWeatherLocalRepo(id,context)
            getAlertWeatherVM(context)
        }
        return res
    }

    fun deleteAllAlertWeatherVM(context: Context): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteAllAlertWeatherLocalRepo(context)
            getAlertWeatherVM(context)
        }
        return res
    }

    fun insertAlertWeatherVM(alertCalendar: AlertCalendar, context: Context): Long{
        var res :Long=0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.insertAlertWeatherLocalRepo(alertCalendar,context)
            getAlertWeatherVM(context)
        }
        return res
    }

}