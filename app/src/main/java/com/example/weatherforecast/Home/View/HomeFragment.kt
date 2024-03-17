package com.example.weatherforecast.Home.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.MapsActivity
import com.example.weatherforecast.Model.Clouds
import com.example.weatherforecast.Model.CurrentWeather
import com.example.weatherforecast.Model.DataState
import com.example.weatherforecast.Model.Main
import com.example.weatherforecast.Model.Weather
import com.example.weatherforecast.Model.WeatherList
//import com.example.weatherforecast.Model.Remote.DetailedWeather
import com.example.weatherforecast.Model.WeatherRepository
import com.example.weatherforecast.Model.Wind
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var hourlyAdapter: HomeFragmentAdapter
    private lateinit var hourlyLayoutManager: LinearLayoutManager
    private lateinit var weeklyAdapter: HomeFragmentAdapter
    private lateinit var weeklyLayoutManager: LinearLayoutManager
    private var lat=0.0
    private var lon=0.0
    private val listOfWeather = MutableList(5) {CurrentWeather()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }

        setHourlyAdapter()
        setWeeklyAdapter()
        getSharedPreferences()
        initViewModel()

        homeFragmentViewModel.getWeatherRemoteVM(
            lat, lon, "a92ea15347fafa48d308e4c367a39bb8", "metric", "en"
        )


        lifecycleScope.launch {
            homeFragmentViewModel.weatherList.collectLatest { value ->
                when(value){
                    is DataState.Success -> {
                            Log.i(TAG, "success: ")
                            updateUI(value)
                    }
                    is DataState.Failure -> {Log.i(TAG, "fail: ")}
                    else -> Log.i(TAG, "loading: ")
                }
            }
        }
    }

    private fun setHourlyAdapter(){
        hourlyAdapter = HomeFragmentAdapter(R.layout.item_hourly)
        hourlyLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.HORIZONTAL, false)
        binding.hourlyRecyclerView.apply {
            adapter = hourlyAdapter
            layoutManager = hourlyLayoutManager
        }
        hourlyAdapter.submitList(listOfWeather)
    }
    private fun setWeeklyAdapter(){
        weeklyAdapter = HomeFragmentAdapter(R.layout.item_weekly)
        weeklyLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.VERTICAL, false)
        binding.weekRecyclerView.apply {
            adapter = weeklyAdapter
            layoutManager = weeklyLayoutManager
        }
        weeklyAdapter.submitList(listOfWeather)
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

    private fun updateUI(value: DataState.Success){
        binding.date.text = value.data.date
        binding.time.text = value.data.time
        binding.city.text = value.data.name
        binding.temperatureValue.text = value.data.main.temp.toInt().toString()
        binding.humidityValue.text = value.data.main.humidity
        binding.pressureValue.text = value.data.main.pressure
        binding.windValue.text = value.data.wind.speed.toString()
        binding.cloudValue.text = value.data.clouds.all.toString()
        binding.weatherStatus.text = value.data.weather[0].description
    }

}