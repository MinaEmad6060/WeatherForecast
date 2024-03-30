package com.example.weatherforecast.Settings.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _sharedFlow = MutableSharedFlow<String>(replay = 1)
    val sharedFlow: SharedFlow<String> = _sharedFlow

    suspend fun sendData(data: String) {
        _sharedFlow.emit(data)
        val i=_sharedFlow.subscriptionCount.value
        Log.i("SharedFlow", "sendData: $i")
    }
}