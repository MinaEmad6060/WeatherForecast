package com.example.weatherforecast.Home.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences.Editor
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.MapsActivity
import com.example.weatherforecast.Model.DataState
import com.example.weatherforecast.Model.WeatherRepository
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private var lat=0.0
    private var lon=0.0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var hourlyAdapter: HomeFragmentHourlyAdapter
    private lateinit var hourlyLayoutManager: LinearLayoutManager
    private lateinit var weeklyAdapter: HomeFragmentWeeklyAdapter
    private lateinit var weeklyLayoutManager: LinearLayoutManager
//    private val listOfWeather = MutableList(5) {CurrentWeather()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlingHomeFAB()
        setHourlyAdapter()
        setWeeklyAdapter()
        getSharedPreferences()
        initViewModel()

        homeFragmentViewModel.getWeatherRemoteVM(
            lat, lon, "a92ea15347fafa48d308e4c367a39bb8", "metric", "en"
        )

        homeFragmentViewModel.getAdditionalWeatherRemoteVM(
            lat, lon, "a92ea15347fafa48d308e4c367a39bb8", "metric", "en", 40
        )


        lifecycleScope.launch {
            homeFragmentViewModel.weatherList.collectLatest { value ->
                when(value){
                    is DataState.Success -> {
                            Log.i(TAG, "success: ")
                            updateWeatherUI(value)
                    }
                    is DataState.Failure -> {Log.i(TAG, "fail: ")}
                    else -> Log.i(TAG, "loading: ")
                }
            }
        }

        lifecycleScope.launch {
            homeFragmentViewModel.additionalWeatherList.collectLatest { value ->
                when(value){
                    is DataState.Success -> {
                        Log.i(TAG, "additionalWeatherList-success: ")
                        val hourlyList = value.data.list.take(9)
                        val weeklyList = value.data.list.filterIndexed { index, _ -> ((index+1) % 8 == 0)}
                        for (i in hourlyList){
                            Log.i(TAG, "print element: ${i.dt_txt}")
                        }
                        hourlyAdapter.submitList(hourlyList)
                        weeklyAdapter.submitList(weeklyList)
                    }
                    is DataState.Failure -> {Log.i(TAG, "additionalWeatherList-fail: ")}
                    else -> Log.i(TAG, "loading: ")
                }
            }
        }
    }


    private fun handlingHomeFAB(){
        binding.fab.setOnClickListener {
            editor = sharedPreferences.edit()
            editor.putString("SelectedFragment", "Home")
            editor.apply()
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }
    }

    private fun setHourlyAdapter(){
        hourlyAdapter = HomeFragmentHourlyAdapter()
        hourlyLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.HORIZONTAL, false)
        binding.hourlyRecyclerView.apply {
            adapter = hourlyAdapter
            layoutManager = hourlyLayoutManager
        }
        //hourlyAdapter.submitList(listOfWeather)
    }
    private fun setWeeklyAdapter(){
        weeklyAdapter = HomeFragmentWeeklyAdapter()
        weeklyLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.VERTICAL, false)
        binding.weekRecyclerView.apply {
            adapter = weeklyAdapter
            layoutManager = weeklyLayoutManager
        }
        //weeklyAdapter.submitList(listOfWeather)
    }


    private fun getSharedPreferences()
    {
        sharedPreferences =
            requireActivity().getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
    }


    private fun initViewModel(){
        homeFragmentViewModelFactory = HomeFragmentViewModelFactory(WeatherRepository)
        homeFragmentViewModel =
            ViewModelProvider(this, homeFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

    private fun updateWeatherUI(value: DataState.Success){
        binding.weatherDate.text = value.data.date
        binding.weatherTime.text = value.data.time
        binding.city.text = value.data.name
        binding.temperatureValue.text = value.data.main.temp.toInt().toString()
        binding.humidityValue.text = value.data.main.humidity
        binding.pressureValue.text = value.data.main.pressure
        binding.windValue.text = value.data.wind.speed.toString()
        binding.cloudValue.text = value.data.clouds.all.toString()
        binding.weatherStatus.text = value.data.weather[0].description
    }

}