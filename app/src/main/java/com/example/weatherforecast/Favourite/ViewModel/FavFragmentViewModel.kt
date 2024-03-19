package com.example.weatherforecast.Favourite.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.Model.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavFragmentViewModel: ViewModel() {
    private val _favWeather= MutableStateFlow<DataState>(DataState.Loading)
    val favWeather: StateFlow<DataState> = _favWeather
}