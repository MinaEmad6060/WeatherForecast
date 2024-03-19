package com.example.weatherforecast.Home.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences.Editor
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import com.example.weatherforecast.Model.AdditionalWeather
import com.example.weatherforecast.Model.DataState
import com.example.weatherforecast.Model.Local.Home.DataStateHome
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.WeatherRepository
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    lateinit var homeWeather: HomeWeather
    private lateinit var roomList : MutableList<AdditionalWeather>


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
        homeFragmentViewModel.getAllHomeWeatherVM(requireActivity())

        lifecycleScope.launch {
            if (isNetworkConnected()){
                homeFragmentViewModel.getAdditionalWeatherRemoteVM(
                    lat, lon, "a92ea15347fafa48d308e4c367a39bb8", "metric", "en", 40
                )
                homeFragmentViewModel.deleteAllHomeWeatherVM(requireActivity())
                homeFragmentViewModel.additionalWeatherList.collectLatest { value ->
                    when(value){
                        is DataState.Success -> {

                            roomList = value.data.list
                            val hourlyList = value.data.list.take(9)
                            val weeklyList =
                                value.data.list.filterIndexed { index, _ -> ((index+1) % 8 == 0)}

                            saveDataToRoom(hourlyList,value,0)
                            saveDataToRoom(weeklyList,value,9)

                            updateOnlineWeatherUI(value)

                            hourlyAdapter.submitList(hourlyList)
                            weeklyAdapter.submitList(weeklyList)
                        }
                        is DataState.Failure -> {Log.i(TAG, "additionalWeatherList-fail: ")}
                        else -> Log.i(TAG, "loading: ")
                    }
                }
            }
            else{

                Snackbar.make(view, "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
                homeFragmentViewModel.homeWeatherList.collectLatest { value ->
                    when(value){
                        is DataStateHome.Success -> {
                            if (value.data.isNotEmpty()){
                                updateOfflineWeatherUI(value)
                                val hourlyList = value.data.filterIndexed { index, _ -> index in 0..8}
                                val weeklyList =
                                    value.data.filterIndexed { index, _ -> index in 9..13}

                                hourlyAdapter.submitList(convertFromRoomToRemote(hourlyList))
                                weeklyAdapter.submitList(convertFromRoomToRemote(weeklyList))
                            }

                        }
                        is DataStateHome.Failure -> {Log.i(TAG, "additionalWeatherList-fail: ")}
                        else -> Log.i(TAG, "loading: ")
                    }
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
    }
    private fun setWeeklyAdapter(){
        weeklyAdapter = HomeFragmentWeeklyAdapter()
        weeklyLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.VERTICAL, false)
        binding.weekRecyclerView.apply {
            adapter = weeklyAdapter
            layoutManager = weeklyLayoutManager
        }
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

    private fun updateOnlineWeatherUI(value: DataState.Success){
        binding.weatherDate.text = value.data.date
        binding.weatherTime.text = value.data.time
        binding.city.text = value.data.city.name
        binding.temperatureValue.text = value.data.list[0].main.temp.toInt().toString()
        binding.humidityValue.text = value.data.list[0].main.humidity
        binding.pressureValue.text = value.data.list[0].main.pressure
        binding.windValue.text = value.data.list[0].wind.speed.toString()
        binding.cloudValue.text = value.data.list[0].clouds.all.toString()
        binding.weatherStatus.text = value.data.list[0].weather[0].description
    }

    private fun updateOfflineWeatherUI(value: DataStateHome.Success){
        binding.weatherDate.text = homeFragmentViewModel.getDateAndTime().split(" ")[0]
        binding.weatherTime.text = homeFragmentViewModel.getDateAndTime().split(" ")[1]
        binding.city.text = value.data[0].cityName
        binding.temperatureValue.text = value.data[0].temperature
        binding.humidityValue.text = value.data[0].humidity
        binding.pressureValue.text = value.data[0].pressure
        binding.windValue.text = value.data[0].windSpeed
        binding.cloudValue.text = value.data[0].clouds
        binding.weatherStatus.text = value.data[0].weatherDescription
    }

    fun convertFromRoomToRemote(homeWeatherList: List<HomeWeather>): MutableList<AdditionalWeather> {
        val additionalWeatherList: MutableList<AdditionalWeather> = mutableListOf()

        for(i in 0..<homeWeatherList.size){
            val additionalWeather = AdditionalWeather()
            additionalWeather.dt_txt=homeWeatherList[i].date+" "+homeWeatherList[i].time
            additionalWeather.main.temp=homeWeatherList[i].temperature.toDouble()
            additionalWeather.main.humidity=homeWeatherList[i].humidity
            additionalWeather.main.pressure=homeWeatherList[i].pressure
            additionalWeather.main.temp_min=homeWeatherList[i].minTemperature.toDouble()
            additionalWeather.main.temp_max=homeWeatherList[i].maxTemperature.toDouble()
            additionalWeather.wind.speed=homeWeatherList[i].windSpeed.toDouble()
            additionalWeather.clouds.all=homeWeatherList[i].clouds.toInt()
            additionalWeather.weather[0].description=homeWeatherList[i].weatherDescription
            additionalWeatherList.add(additionalWeather)
        }
        return additionalWeatherList
    }

    suspend fun saveDataToRoom(
        myList: List<AdditionalWeather>, value: DataState.Success, start: Int){
        for(i in 0..<myList.size){
            homeWeather = HomeWeather(i+start)

            homeWeather.date = myList[i].dt_txt.split(" ")[0]
            homeWeather.time = myList[i].dt_txt.split(" ")[1]
            homeWeather.cityName = value.data.city.name
            homeWeather.weatherDescription = myList[i].weather[0].description
            homeWeather.weatherIcon = myList[i].weather[0].icon
            homeWeather.temperature = myList[i].main.temp.toInt().toString()
            homeWeather.minTemperature = myList[i].main.temp_min.toInt().toString()
            homeWeather.maxTemperature = myList[i].main.temp_max.toInt().toString()
            homeWeather.humidity = myList[i].main.humidity
            homeWeather.pressure = myList[i].main.pressure
            homeWeather.windSpeed = myList[i].wind.speed.toString()
            homeWeather.clouds = myList[i].clouds.all.toString()

            homeFragmentViewModel.insertAllHomeWeatherVM(homeWeather,requireActivity())
        }
    }
    fun isNetworkConnected(): Boolean {
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}