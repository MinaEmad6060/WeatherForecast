package com.example.weatherforecast.Alert.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Alert.DataStateAlertRoom
import com.example.weatherforecast.Model.Remote.Alert.DataStateAlertRemote
import com.example.weatherforecast.Model.Repo.Alert.InterAlertRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertFragmentViewModel(private var repo: InterAlertRepo): ViewModel() {
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

    fun getAlertWeatherVM(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAlertWeatherLocalRepo()
                .catch {
                    _alertWeatherRoom.value = DataStateAlertRoom.Failure(it)
                }
                .collect{
                    _alertWeatherRoom.value = DataStateAlertRoom.Success(it)
                }
        }
    }

    suspend fun deleteAlertWeatherVM(id: String): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteAlertWeatherLocalRepo(id)
            getAlertWeatherVM()
        }.await()
        return res
    }

    suspend fun insertAlertWeatherVM(alertCalendar: AlertCalendar): Long{
        var res :Long=0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.insertAlertWeatherLocalRepo(alertCalendar)
            getAlertWeatherVM()
        }.await()
        return res
    }

}