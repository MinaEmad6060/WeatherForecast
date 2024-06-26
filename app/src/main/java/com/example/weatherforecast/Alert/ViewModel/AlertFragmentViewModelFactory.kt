package com.example.weatherforecast.Alert.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Model.Repo.Alert.InterAlertRepo


@Suppress("UNCHECKED_CAST")
class AlertFragmentViewModelFactory (var repository: InterAlertRepo)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertFragmentViewModel::class.java)){
            AlertFragmentViewModel(repository) as T
        }else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}