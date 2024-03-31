package com.example.weatherforecast.Settings.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.weatherforecast.Main.MainActivity
import com.example.weatherforecast.Main.MapsActivity
import com.example.weatherforecast.Main.Utils.Companion.language
import com.example.weatherforecast.R
import com.example.weatherforecast.Settings.ViewModel.CentralSharedFlow
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import kotlinx.coroutines.CoroutineScope
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
//    private lateinit var centralSharedFlow: CentralSharedFlow
//    private lateinit var externalScope: CoroutineScope
    var itemSelected="a"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSharedPreferences()
        val locationItems = listOf("GPS", "Map")
        val languageItems = listOf("EN", "AR")
        val temperatureItems = listOf("°C", "°F","K")
        val windSpeedItems = listOf("m/s", "mile/h")

        updateDropList(binding.locationList, locationItems, "location")
        updateDropList(binding.languageList, languageItems, "language")
        updateDropList(binding.temperatureList, temperatureItems, "temperature")
        updateDropList(binding.windList, windSpeedItems, "wind")

    }

    fun updateDropList(dropDownList: AutoCompleteTextView, items: List<String>, category: String){
        val adapter = ArrayAdapter(requireActivity(), R.layout.item_drop_list,items)
        dropDownList.setAdapter(adapter)
        handlingDropDownListClick(dropDownList, category)
        Log.i("setting", "updateDropList: $itemSelected")

    }


    private fun handlingDropDownListClick(dropDownList: AutoCompleteTextView, category: String){
        dropDownList.onItemClickListener = AdapterView.OnItemClickListener{
                adapterView, view, position, id ->
            itemSelected = adapterView.getItemAtPosition(position).toString()
            when(category){
                "location" -> {
                    if(itemSelected == "GPS"){
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                    }else if(itemSelected == "Map"){
                        startActivity(Intent(requireActivity(), MapsActivity::class.java))
                    }
                }
                "language" -> {
                        setLocale(itemSelected.toLowerCase())
                        language=itemSelected.toLowerCase()
                        Log.i("newShare", "handlingDropDownListClick: $language")
                        editor.putString("languageSettings",itemSelected)
                        editor.apply()
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
                "temperature" -> {
                    if (itemSelected=="°C"){
                        editor.putString("temperatureSettings","metric")
                        editor.putString("degreeSettings",itemSelected)
                    }else if (itemSelected=="K"){
                        editor.putString("temperatureSettings","standard")
                        editor.putString("degreeSettings",itemSelected)
                    }else if (itemSelected=="°F"){
                        editor.putString("temperatureSettings","imperial")
                        editor.putString("degreeSettings",itemSelected)
                    }

                    editor.apply()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
                "wind" -> {
                    if (itemSelected=="m/s"){
                        editor.putString("measureSettings",itemSelected)
                    }else if (itemSelected=="mile/h") {
                        editor.putString("measureSettings", itemSelected)
                    }

                    editor.apply()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            }
            Toast.makeText(requireActivity(), "item $itemSelected", Toast.LENGTH_SHORT).show()
        }
    }



    private fun getSharedPreferences()
    {
        sharedPreferences =
            requireActivity().getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
//        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
//        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
        editor=sharedPreferences.edit()
//        editor.putString("SelectedFragment","Home")
//        editor.apply()
    }

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}