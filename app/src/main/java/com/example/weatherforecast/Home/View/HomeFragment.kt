package com.example.weatherforecast.Home.View

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.WeatherRepository
import com.example.weatherforecast.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentViewModelFactory=HomeFragmentViewModelFactory(WeatherRepository)
        homeFragmentViewModel=
            ViewModelProvider(this,homeFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)

        homeFragmentViewModel.getWeatherRemoteVM(
            31.26863,30.0059383,"a92ea15347fafa48d308e4c367a39bb8","metric","en")

        homeFragmentViewModel.weatherList.observe(requireActivity()) {
                value ->
            Log.i(TAG, "weatherList: ${value.main.temp}")
            binding.date.text = value.date
            binding.time.text = value.time
            binding.city.text = value.name
            binding.temperatureValue.text = value.main.temp.toInt().toString()
            binding.humidityValue.text = value.main.humidity
            binding.pressureValue.text = value.main.pressure
            binding.windValue.text = value.wind.speed.toString()
            binding.cloudValue.text = value.clouds.all.toString()
            binding.weatherStatus.text = value.weather.get(0).description
        }
    }

}