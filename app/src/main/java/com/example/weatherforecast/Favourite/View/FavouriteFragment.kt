package com.example.weatherforecast.Favourite.View

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModel
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModelFactory
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Main.MainActivity
import com.example.weatherforecast.Main.MapsActivity
import com.example.weatherforecast.Main.Utils.Companion.isNetworkConnected
import com.example.weatherforecast.Main.Utils.Companion.key
import com.example.weatherforecast.Main.Utils.Companion.lat
import com.example.weatherforecast.Main.Utils.Companion.lon
import com.example.weatherforecast.Model.Local.Fav.DataStateFavRoom
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Remote.Home.DataStateHomeRemote
import com.example.weatherforecast.databinding.FragmentFavouriteBinding
import com.example.weatherforecast.di.AppContainer
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    private val TAG = "FavFragment"
    private var language="EN"
    private var temperature="metric"
    private var degree="°C"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favAdapter: FavouriteFragmentAdapter
    private lateinit var favLayoutManager: LinearLayoutManager
    private lateinit var favFragmentViewModelFactory: FavFragmentViewModelFactory
    private lateinit var favFragmentViewModel: FavFragmentViewModel
    private lateinit var homeFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var favWeather: FavWeather
    private lateinit var appContainer: AppContainer
    private lateinit var favContext: Context




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        favContext=context
        appContainer = AppContainer(context.applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlingFavFAB(view)
        setFavAdapter()
        getSharedPreferences()
        initHomeViewModel()
        initFavViewModel()
        val sharedPreferences = sharedPreferences.getString("goToFragment","")


        favFragmentViewModel.getFavWeatherVM()



    lifecycleScope.launch {
        if(sharedPreferences.equals("Fav")){
            editor.putString("goToFragment","")
            editor.apply()
            homeFragmentViewModel.getAdditionalWeatherRemoteVM(
                lat, lon, key, temperature, language, 1
            )
            homeFragmentViewModel.additionalWeatherList.collectLatest { value ->
                when(value){
                    is DataStateHomeRemote.Success -> {
                        favWeather = FavWeather()
                        favWeather.cityName = value.data.city.name
                        favWeather.temperature = value.data.list[0].main.temp
                        favWeather.img = value.data.list[0].weather[0].icon
                        favWeather.lat = lat
                        favWeather.lon = lon
                        favWeather.units = degree
                        favFragmentViewModel.insertFavWeatherVM(favWeather)
                    }
                    is DataStateHomeRemote.Failure -> {
                        Snackbar.make(view, "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
                    }
                    else -> {}

                }
            }

        }
    }




        lifecycleScope.launch {
            favFragmentViewModel.favWeather.collectLatest { value ->
                when(value){
                    is DataStateFavRoom.Success -> {
                        if (value.data.isNotEmpty()) {
                            binding.noFavImg.visibility = View.GONE
                            binding.favRecyclerView.visibility = View.VISIBLE
                        }else{
                            binding.noFavImg.visibility = View.VISIBLE
                            binding.favRecyclerView.visibility = View.GONE
                        }
                            favAdapter.submitList(value.data)
                    }
                    is DataStateFavRoom.Failure -> {
                        Snackbar.make(view, "Can't access favourite items, try again!", Snackbar.LENGTH_LONG).show()
                    }
                    else ->
                    {
                        binding.noFavImg.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun handlingFavFAB(view: View){
        binding.favFab.setOnClickListener {
            if (isNetworkConnected(favContext)){
                editor.putString("goToFragment","")
                editor.putString("SelectedFragment","Fav")
                editor.apply()
                startActivity(Intent(requireActivity(), MapsActivity::class.java))
            }else{
                Snackbar.make(view, "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
            }

        }
    }

    private fun setFavAdapter(){
        favAdapter = FavouriteFragmentAdapter({
            delFavWeather ->
            showAlert("Delete favourite Item"
                ,"Are you sure you want delete this City?"
                ,delFavWeather,requireActivity())},
            { favDetails -> navToFavDetails(favDetails) }
        )
        favLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.VERTICAL, false)
        binding.favRecyclerView.apply {
            adapter = favAdapter
            layoutManager = favLayoutManager
        }
    }

    private fun initFavViewModel(){
        favFragmentViewModelFactory = appContainer.favFactory
        favFragmentViewModel =
            ViewModelProvider(this, favFragmentViewModelFactory)
                .get(FavFragmentViewModel::class.java)
    }

    private fun initHomeViewModel(){
        homeFragmentViewModelFactory = appContainer.homeFactory
        homeFragmentViewModel =
            ViewModelProvider(this, homeFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

    private fun getSharedPreferences()
    {
        sharedPreferences =
            requireActivity().getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
        language = sharedPreferences.getString("languageSettings", "EN")!!.toLowerCase()
        temperature = sharedPreferences.getString("temperatureSettings", "metric")!!
        degree = sharedPreferences.getString("degreeSettings", "°C")!!
        editor = sharedPreferences.edit()
        editor.apply()

    }

    fun navToFavDetails(favDetails: FavWeather){
        if(isNetworkConnected(requireActivity())){
            lat=favDetails.lat
            lon=favDetails.lon
            editor.putString("latitude",favDetails.lat.toString())
            editor.putString("longitude",favDetails.lon.toString())
            editor.putString("lastFragmentTag","HomeFragment")
            editor.apply()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }else Snackbar.make(requireActivity().findViewById(android.R.id.content),
            "Please Check Your Internet Connection..", Snackbar.LENGTH_LONG).show()
    }


    fun showAlert(title: String, message: String, delFavWeather: FavWeather,context: Context):Int {
        val builder = AlertDialog.Builder(context)
        var result =0
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            lifecycleScope.launch {
                result = favFragmentViewModel.deleteFavWeatherVM(delFavWeather)
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> }
        builder.show()
        return result
    }

}