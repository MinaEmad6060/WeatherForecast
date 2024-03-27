package com.example.weatherforecast.Home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Model.Repo.Home.InterHomeRepo
import com.example.weatherforecast.Model.Repo.InterWeatherRepository

@Suppress("UNCHECKED_CAST")
class HomeFragmentViewModelFactory(var repository: InterHomeRepo)
        : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if(modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)){
                HomeFragmentViewModel(repository) as T
            }else{
                throw IllegalArgumentException("View Model Class Not Found")
            }
        }
}