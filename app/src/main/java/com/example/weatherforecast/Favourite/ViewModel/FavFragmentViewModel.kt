package com.example.weatherforecast.Favourite.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.Repo.InterWeatherRepository
import com.example.weatherforecast.Model.Local.Fav.DataStateFavRoom
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavFragmentViewModel(private var repo: InterWeatherRepository): ViewModel() {
    private val _favWeather= MutableStateFlow<DataStateFavRoom>(DataStateFavRoom.Loading)
    val favWeather: StateFlow<DataStateFavRoom> = _favWeather


    fun getFavWeatherVM(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFavWeatherLocalRepo(context)
                .catch {
                    Log.i("vmRoom", "getAllHomeWeatherVM: fail")
                    _favWeather.value = DataStateFavRoom.Failure(it)
                }
                .collect{
                    Log.i("vmRoom", "getAllHomeWeatherVM: success")
                    _favWeather.value = DataStateFavRoom.Success(it)
                }
        }
    }

    fun deleteFavWeatherVM(favWeather: FavWeather, context: Context): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteFavWeatherLocalRepo(favWeather,context)
            getFavWeatherVM(context)
        }
        return res
    }

    fun deleteAllFavWeatherVM(context: Context): Int{
        var res =0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.deleteAllFavWeatherLocalRepo(context)
            getFavWeatherVM(context)        }
        return res
    }

    fun insertFavWeatherVM(favWeather: FavWeather, context: Context): Long{
        var res :Long=0
        viewModelScope.async(Dispatchers.IO) {
            res = repo.insertFavWeatherLocalRepo(favWeather,context)
            getFavWeatherVM(context)
        }
        return res
    }

}