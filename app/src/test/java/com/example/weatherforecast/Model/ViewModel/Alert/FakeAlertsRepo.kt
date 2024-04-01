package com.example.weatherforecast.Model.ViewModel.Alert


import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import com.example.weatherforecast.Model.Repo.Alert.InterAlertRepo
import kotlinx.coroutines.flow.Flow


class FakeAlertsRepo (
    private var fakeAlertRemoteDataSource: FakeAlertRemoteDataSource,
    private var fakeAlertsLocalDataSource: FakeAlertsLocalDataSource
) : InterAlertRepo {

    override suspend fun getAlertWeatherRemoteRepo(
        lat: Double,
        lon: Double,
        key: String
    ): Flow<OneCallAlert> {
        return fakeAlertRemoteDataSource.getAlertWeatherRemote(lat,lon,key)
    }

    override suspend fun getAlertWeatherLocalRepo(): Flow<List<AlertCalendar>> {
        return fakeAlertsLocalDataSource.getAlertWeatherLocal()
    }

    override suspend fun deleteAlertWeatherLocalRepo(id: String): Int {
        return fakeAlertsLocalDataSource.deleteAlertWeatherLocal(id)
    }

    override suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar): Long {
        return fakeAlertsLocalDataSource.insertAlertWeatherLocal(alertCalendar)
    }
}