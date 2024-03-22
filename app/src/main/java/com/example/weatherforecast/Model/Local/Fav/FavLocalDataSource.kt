package com.example.weatherforecast.Model.Local.Fav

import android.content.Context
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavLocalDataSource: InterFavLocalDataSource {


    override fun getFavWeatherLocal(context: Context): StateFlow<List<FavWeather>> {
        val favWeather: List<FavWeather> = mutableListOf()
        val stateFlow = MutableStateFlow(favWeather)
        CoroutineScope(Dispatchers.Default).launch {
            getRoomInstance(context).getFavWeather().collect {
                stateFlow.value=it
            }}
        return stateFlow
    }

    override suspend fun deleteFavWeatherLocal(favWeather: FavWeather, context: Context): Int {
        return getRoomInstance(context).delete(favWeather)
    }

    override suspend fun insertFavWeatherLocal(favWeather: FavWeather, context: Context): Long {
        return getRoomInstance(context).insert(favWeather)
    }

    override suspend fun deleteAllFavWeatherLocal(context: Context): Int {
        return getRoomInstance(context).deleteAll()
    }


    private var roomRef: FavWeatherDAO? = null
    @Synchronized
    override fun getRoomInstance(context: Context): FavWeatherDAO {
        if (roomRef == null) {
            val database = dbFav.getInstance(context)
            roomRef = database.getFavWeatherDao()
        }
        return roomRef!!
    }

}