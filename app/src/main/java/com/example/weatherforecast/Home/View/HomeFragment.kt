package com.example.weatherforecast.Home.View

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences.Editor
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Main.MapsActivity
import com.example.weatherforecast.Main.Utils.Companion.backGroundDesc
import com.example.weatherforecast.Main.Utils.Companion.createCentralSharedLanguage
import com.example.weatherforecast.Main.Utils.Companion.initBackGround
import com.example.weatherforecast.Main.Utils.Companion.isNetworkConnected
import com.example.weatherforecast.Main.Utils.Companion.key
import com.example.weatherforecast.Main.Utils.Companion.lat
import com.example.weatherforecast.Main.Utils.Companion.lon
import com.example.weatherforecast.Main.Utils.Companion.setNumberLocale
import com.example.weatherforecast.Model.Remote.Home.AdditionalWeather
import com.example.weatherforecast.Model.Remote.Home.DataStateHomeRemote
import com.example.weatherforecast.Model.Local.Home.DataStateHomeRoom
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.di.AppContainer
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private var temperature="metric"
    private var degree="°C"
    private var measure="m/s"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var hourlyAdapter: HomeFragmentHourlyAdapter
    private lateinit var hourlyLayoutManager: LinearLayoutManager
    private lateinit var weeklyAdapter: HomeFragmentWeeklyAdapter
    private lateinit var weeklyLayoutManager: LinearLayoutManager
    private lateinit var homeWeather: HomeWeather
    private lateinit var appContainer: AppContainer
    private var homeLanguage=""



    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContainer = AppContainer(context.applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeLanguage=createCentralSharedLanguage(lifecycleScope,resources)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor= Color.TRANSPARENT
        }
        ObjectAnimator.ofInt(binding.progressBar, "progress", 6)
            .start()
        handlingHomeFAB(view)
        setHourlyAdapter()
        setWeeklyAdapter()
        getSharedPreferences()
        initViewModel()

        homeFragmentViewModel.getAllHomeWeatherVM()

        lifecycleScope.launch {
            if (isNetworkConnected(requireActivity())){
                Log.i("lastTesr", "onViewCreated: ")
                homeFragmentViewModel.getAdditionalWeatherRemoteVM(
                    lat, lon, key, temperature, homeLanguage, 40
                )
                homeFragmentViewModel.deleteAllHomeWeatherVM()
                homeFragmentViewModel.additionalWeatherList.collectLatest { value ->
                    when(value){
                        is DataStateHomeRemote.Success -> {
                            backGroundDesc=value.data.list[0].weather[0].icon
                            editor.putString("backGround",backGroundDesc)
                            editor.apply()
                            initBackGround(backGroundDesc, requireActivity())
                            val hourlyList = value.data.list.take(9)
                            val weeklyList =
                                value.data.list.filterIndexed { index, _ -> ((index+1) % 8 == 0)}

                            saveDataToRoom(hourlyList,value,0)
                            saveDataToRoom(weeklyList,value,9)

                            updateOnlineWeatherUI(value)
                            hourlyList[0].units = degree
                            weeklyList[0].units = degree
                            hourlyAdapter.submitList(hourlyList)
                            weeklyAdapter.submitList(weeklyList)
                            binding.progressBar.visibility  = View.GONE
                            binding.All.visibility = View.VISIBLE

                        }
                        is DataStateHomeRemote.Failure -> {
                            binding.All.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(view, "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
                        }
                        else -> {
                            binding.All.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                    }
                }
            }
            else{
                binding.progressBar.visibility = View.GONE
                Snackbar.make(view, "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
                homeFragmentViewModel.homeWeatherList.collectLatest { value ->
                    when(value){
                        is DataStateHomeRoom.Success -> {
                            if (value.data.isNotEmpty()){
                                updateOfflineWeatherUI(value)
                                val hourlyList = value.data.filterIndexed { index, _ -> index in 0..8}
                                val weeklyList =
                                    value.data.filterIndexed { index, _ -> index in 9..13}

                                hourlyAdapter.submitList(convertFromRoomToRemote(hourlyList))
                                weeklyAdapter.submitList(convertFromRoomToRemote(weeklyList))
                            }

                        }
                        is DataStateHomeRoom.Failure -> {
                            Snackbar.make(view, "Can't display previous items, try again!", Snackbar.LENGTH_LONG).show()
                        }
                        else -> Log.i(TAG, "Loading... ")

                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editor.putString("goToFragment","")
        editor.apply()
    }


    private fun handlingHomeFAB(view: View){
        binding.fab.setOnClickListener {
            if (isNetworkConnected(requireActivity())) {
                editor.putString("goToFragment", "")
                editor.apply()
                startActivity(Intent(requireActivity(), MapsActivity::class.java))
            }else{
                Snackbar.make(view, "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
            }
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
        lat = sharedPreferences.getString("latitude", "31.26863")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "30.0059383")!!.toDouble()
        temperature = sharedPreferences.getString("temperatureSettings", "metric")!!
        degree = sharedPreferences.getString("degreeSettings", "°C")!!
        measure = sharedPreferences.getString("measureSettings", "m/s")!!
        editor=sharedPreferences.edit()
        editor.putString("SelectedFragment","Home")
        editor.apply()
    }


    private fun initViewModel(){
        homeFragmentViewModelFactory = appContainer.homeFactory
        homeFragmentViewModel =
            ViewModelProvider(this, homeFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }


    @SuppressLint("SetTextI18n")
    private fun updateOnlineWeatherUI(value: DataStateHomeRemote.Success){
        binding.weatherDate.text = value.data.date
        binding.weatherTime.text = value.data.time
        binding.city.text = value.data.city.name
        Glide.with(requireActivity())
            .load("https://openweathermap.org/img/wn/"
                    +value.data.list[0].weather[0].icon+"@2x.png")
            .into(binding.mainWeatherImage)
        editor.putString("backGround", value.data.list[0].weather[0].icon)
        editor.apply()
        binding.temperatureValue.text = setNumberLocale(value.data.list[0].main.temp.toInt(),degree)
        binding.humidityValue.text = value.data.list[0].main.humidity
        binding.pressureValue.text = value.data.list[0].main.pressure
        if (measure=="mile/h"){
            binding.windValue.text = String.format("%.1f", ((value.data.list[0].wind.speed)*2.23694))+" "+measure
        }else{
            binding.windValue.text = value.data.list[0].wind.speed.toString()+" "+measure
        }
        binding.cloudValue.text = value.data.list[0].clouds.all.toString()
        binding.weatherStatus.text = value.data.list[0].weather[0].description
    }

    @SuppressLint("SetTextI18n")
    private fun updateOfflineWeatherUI(value: DataStateHomeRoom.Success){
        binding.weatherDate.text = homeFragmentViewModel.getDateAndTime().split(" ")[0]
        binding.weatherTime.text = homeFragmentViewModel.getDateAndTime().split(" ")[1]
        binding.city.text = value.data[0].cityName
        Glide.with(requireActivity())
            .load("https://openweathermap.org/img/wn/"
                    +value.data[0].weatherIcon+"@2x.png")
            .into(binding.mainWeatherImage)
        binding.temperatureValue.text = value.data[0].temperature+" "+value.data[0].units
        binding.humidityValue.text = value.data[0].humidity
        binding.pressureValue.text = value.data[0].pressure
        if (measure=="mile/h"&&homeLanguage=="en"){
            binding.windValue.text = String.format("%.1f", ((value.data[0].windSpeed.toDouble())*2.23694))+" "+measure
        }else{
            binding.windValue.text = value.data[0].windSpeed+" "+measure
        }
        binding.cloudValue.text = value.data[0].clouds
        binding.weatherStatus.text = value.data[0].weatherDescription

    }


    //
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
            if(homeLanguage=="en"){
                additionalWeather.wind.speed=homeWeatherList[i].windSpeed.toDouble()
            }
            additionalWeather.clouds.all=homeWeatherList[i].clouds.toInt()
            additionalWeather.weather[0].description=homeWeatherList[i].weatherDescription
            additionalWeather.weather[0].icon=homeWeatherList[i].weatherIcon
            additionalWeather.units = homeWeatherList[0].units
            additionalWeatherList.add(additionalWeather)
        }
        return additionalWeatherList
    }

    suspend fun saveDataToRoom(
        myList: List<AdditionalWeather>, value: DataStateHomeRemote.Success, start: Int){
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
            if (measure=="mile/h"){
                homeWeather.windSpeed = String.format("%.1f", ((myList[i].wind.speed)*2.23694))
            }
            homeWeather.clouds = myList[i].clouds.all.toString()
            homeWeather.units = degree
            homeFragmentViewModel.insertAllHomeWeatherVM(homeWeather)
        }
    }
}