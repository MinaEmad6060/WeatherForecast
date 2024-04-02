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
    private val _languageFlow = MutableSharedFlow<String>(replay = 1)
    val languageFlow: SharedFlow<String> = _languageFlow
    val centralLanguage= sharedPreferences.getString("languageSettings", "")!!.toLowerCase()
    init {
        externalScope.launch {
            while (true){
                _languageFlow.emit(centralLanguage)
                delay(3000)
            }
        }
    }
}
