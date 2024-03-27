package com.example.weatherforecast.Model.Repo.Home

import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Local.Home.InterHomeLocalDataSource
import com.example.weatherforecast.Model.Remote.Home.CurrentWeather
import com.example.weatherforecast.Model.Remote.Home.InterRemoteDataSource
import kotlinx.coroutines.flow.Flow

class HomeRepo(
    private var remoteWeather: InterRemoteDataSource,
    private var roomHomeWeather: InterHomeLocalDataSource
): InterHomeRepo {

    override suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double,
        lon: Double,
        key: String,
        units: String,
        lang: String,
        cnt: Int
    ): Flow<CurrentWeather> =
        remoteWeather.getAdditionalWeatherRemote(lat,lon,key,units,lang,cnt)

    override suspend fun getAllHomeWeatherLocalRepo(): Flow<List<HomeWeather>> {
        return roomHomeWeather.getAllHomeWeatherLocal()
    }

    override suspend fun deleteAllHomeWeatherLocalRepo(): Int {
        return roomHomeWeather.deleteAllHomeWeatherLocal()
    }

    override suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather): Long {
        return roomHomeWeather.insertAllHomeWeatherLocal(homeWeather)
    }
}