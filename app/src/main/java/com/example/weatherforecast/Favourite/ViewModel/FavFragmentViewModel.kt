package com.example.weatherforecast.Favourite.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.Local.Fav.DataStateFavRoom
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.Fav.InterFavRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavFragmentViewModel(private var repo: InterFavRepo): ViewModel() {
    private val _favWeather= MutableStateFlow<DataStateFavRoom>(DataStateFavRoom.Loading)
    val favWeather: StateFlow<DataStateFavRoom> = _favWeather


    fun getFavWeatherVM(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFavWeatherLocalRepo()
                .catch {
                    _favWeather.value = DataStateFavRoom.Failure(it)
                }
                .collect{
                    _favWeather.value = DataStateFavRoom.Success(it)
                }
        }
    }

    suspend fun deleteFavWeatherVM(favWeather: FavWeather): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteFavWeatherLocalRepo(favWeather)
            getFavWeatherVM()
        }.await()
        return res
    }

    suspend fun insertFavWeatherVM(favWeather: FavWeather): Long{
        var res :Long=0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.insertFavWeatherLocalRepo(favWeather)
            getFavWeatherVM()
        }.await()

        return res
    }

}