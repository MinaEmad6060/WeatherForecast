package com.example.weatherforecast.Model.Local.Alert

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertLocalDataSource(private val dao: AlertWeatherDAO): InterAlertLocalDataSource {

    override fun getAlertWeatherLocal(): Flow<List<AlertCalendar>> {
        return dao.getAlertWeather()
//        val alertWeather: List<AlertCalendar> = mutableListOf()
//        val stateFlow = MutableStateFlow(alertWeather)
//        CoroutineScope(Dispatchers.Default).launch {
//            getRoomInstance(context).getAlertWeather().collect {
//                stateFlow.value=it
//            }}
//        return stateFlow
    }

//    override suspend fun deleteAlertWeatherLocal(id: Int, context: Context): Int {
//        return getRoomInstance(context).delete(id)
//    }
    override suspend fun deleteAlertWeatherLocal(id: String): Int {
        return dao.delete(id)
    }

    override suspend fun insertAlertWeatherLocal(alertCalendar: AlertCalendar): Long {
        return dao.insert(alertCalendar)
    }

//    override suspend fun deleteAllAlertWeatherLocal(context: Context): Int {
//        return dao.deleteAll()
//    }


//    private var roomRef: AlertWeatherDAO? = null
//    @Synchronized
//    override fun getRoomInstance(context: Context): AlertWeatherDAO {
//        if (roomRef == null) {
//            val database = dbAlert.getInstance(context)
//            roomRef = database.getAlertWeatherDao()
//        }
//        return roomRef!!
//    }

}