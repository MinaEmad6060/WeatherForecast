package com.example.weatherforecast.Model.ViewModel.Alert


import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import com.example.weatherforecast.Model.Repo.Alert.InterAlertRepo
import kotlinx.coroutines.flow.Flow


class FakeAlertRepo (
//    private var fakeAlertRemoteDataSource: FakeAlertRemoteDataSource,
    private var fakeAlertLocalDataSource: FakeAlertLocalDataSource
) : InterAlertRepo {

//    override suspend fun getAlertWeatherRemoteRepo(
//        lat: Double,
//        lon: Double,
//        key: String
//    ): Flow<OneCallAlert> {
//        return fakeAlertRemoteDataSource.getAlertWeatherRemote(lat,lon,key)
//    }

    override suspend fun getAlertWeatherLocalRepo(): Flow<List<AlertCalendar>> {
        return fakeAlertLocalDataSource.getAlertWeatherLocal()
    }

    override suspend fun deleteAlertWeatherLocalRepo(id: String): Int {
        return fakeAlertLocalDataSource.deleteAlertWeatherLocal(id)
    }

    override suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar): Long {
        return fakeAlertLocalDataSource.insertAlertWeatherLocal(alertCalendar)
    }
}