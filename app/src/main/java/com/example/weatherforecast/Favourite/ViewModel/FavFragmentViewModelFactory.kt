package com.example.weatherforecast.Favourite.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Model.Repo.Fav.InterFavRepo
import com.example.weatherforecast.Model.Repo.InterWeatherRepository


@Suppress("UNCHECKED_CAST")
class FavFragmentViewModelFactory (var repository: InterFavRepo)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavFragmentViewModel::class.java)){
            FavFragmentViewModel(repository) as T
        }else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}