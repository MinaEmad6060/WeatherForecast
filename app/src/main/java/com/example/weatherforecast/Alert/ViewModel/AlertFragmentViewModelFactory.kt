//package com.example.weatherforecast.Alert.ViewModel
//
//import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModel
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.weatherforecast.Model.InterWeatherRepository
//
//
//@Suppress("UNCHECKED_CAST")
//class FavFragmentViewModelFactory (var repository: InterWeatherRepository)
//    : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return if(modelClass.isAssignableFrom(FavFragmentViewModel::class.java)){
//            FavFragmentViewModel(repository) as T
//        }else{
//            throw IllegalArgumentException("View Model Class Not Found")
//        }
//    }
//}