package com.example.weatherforecast.Settings.ViewModel
import com.example.weatherforecast.Main.Utils.Companion.sharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CentralSharedFlow(
    externalScope: CoroutineScope,
) {
    private val _tickFlow = MutableSharedFlow<String>(replay = 3)
    val tickFlow: SharedFlow<String> = _tickFlow
    val centralLanguage= sharedPreferences.getString("languageSettings", "")!!.toLowerCase()
    init {
        externalScope.launch {
            while (true){
                _tickFlow.emit(centralLanguage)
                //Log.i("newShare", "newShare: $centralLanguage")
                delay(3000)
            }
        }
    }
}
