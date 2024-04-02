package com.example.weatherforecast.Model.Repo.Alert


import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource
import com.example.weatherforecast.Model.Remote.Alert.InterAlertRemoteDataSource
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import kotlinx.coroutines.flow.Flow


class AlertRepo(
    private var remoteAlert: InterAlertRemoteDataSource,
    private var roomAlertWeather: InterAlertLocalDataSource
): InterAlertRepo {


    override suspend fun getAlertWeatherRemoteRepo(
        lat: Double, lon: Double, key: String): Flow<OneCallAlert> =
        remoteAlert.getAlertWeatherRemote(lat,lon,key)


    override suspend fun getAlertWeatherLocalRepo(): Flow<List<AlertCalendar>> {
        return roomAlertWeather.getAlertWeatherLocal()
    }
    override suspend fun deleteAlertWeatherLocalRepo(id: String): Int {
        return roomAlertWeather.deleteAlertWeatherLocal(id)
    }

    override suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar): Long {
        return roomAlertWeather.insertAlertWeatherLocal(alertCalendar)
    }
}