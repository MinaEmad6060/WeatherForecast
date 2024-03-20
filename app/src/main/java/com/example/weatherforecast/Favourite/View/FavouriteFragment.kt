package com.example.weatherforecast.Favourite.View

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModel
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModelFactory
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.MapsActivity
import com.example.weatherforecast.Model.Local.Fav.DataStateFavRoom
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Remote.DataStateRemote
import com.example.weatherforecast.Model.WeatherRepository
import com.example.weatherforecast.databinding.FragmentFavouriteBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    private val TAG = "FavFragment"
    private var lat=0.0
    private var lon=0.0
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlingFavFAB()
        setFavAdapter()
        getSharedPreferences()
        initHomeViewModel()
        initFavViewModel()
        val sharedPreferences = sharedPreferences.getString("goToFragment","")
        Log.i("goto", "goto: ${sharedPreferences}")


        //favFragmentViewModel.deleteAllFavWeatherVM(requireActivity())
        //favFragmentViewModel.insertAllHomeWeatherVM(favWeather,requireActivity())
        favFragmentViewModel.getFavWeatherVM(requireActivity())

        Log.i(TAG, "lat&lon $lat $lon")


    lifecycleScope.launch {
        if(sharedPreferences.equals("Fav")){
            editor.putString("goToFragment","")
            editor.apply()
            homeFragmentViewModel.getAdditionalWeatherRemoteVM(
                lat, lon, "a92ea15347fafa48d308e4c367a39bb8", "metric", "en", 1
            )
            homeFragmentViewModel.additionalWeatherList.collectLatest { value ->
                when(value){
                    is DataStateRemote.Success -> {
                        Log.i(TAG, "Remote Success: ")
                        favWeather = FavWeather()
                        favWeather.cityName = value.data.city.name
                        favWeather.temperature = value.data.list[0].main.temp
                        favWeather.lat = lat
                        favWeather.lon = lon
                        favFragmentViewModel.insertFavWeatherVM(favWeather,requireActivity())
                    }
                    is DataStateRemote.Failure -> {Log.i(TAG, "Remote fail: ")}
                    else -> Log.i(TAG, "Remote loading: ")
                }
            }

        }else{
            Log.i("goto", "if goto: fail")
        }

    }




        lifecycleScope.launch {
            favFragmentViewModel.favWeather.collectLatest { value ->
                when(value){
                    is DataStateFavRoom.Success -> {
                        Log.i(TAG, "favWeather-success: $lat $lon")
                        favAdapter.submitList(value.data)
                    }
                    is DataStateFavRoom.Failure -> {Log.i(TAG, "favWeather-fail: ")}
                    else -> Log.i(TAG, "favWeather-loading: ")
                }
            }
        }

    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        editor.putString("goToFragment","")
//        editor.apply()
//    }

    private fun handlingFavFAB(){
        binding.favFab.setOnClickListener {
            editor.putString("goToFragment","")
            editor.apply()
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }
    }

    private fun setFavAdapter(){
        favAdapter = FavouriteFragmentAdapter()
        favLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.VERTICAL, false)
        binding.favRecyclerView.apply {
            adapter = favAdapter
            layoutManager = favLayoutManager
        }
        //favAdapter.submitList(listOfWeather)
    }

    private fun initFavViewModel(){
        favFragmentViewModelFactory = FavFragmentViewModelFactory(WeatherRepository)
        favFragmentViewModel =
            ViewModelProvider(this, favFragmentViewModelFactory)
                .get(FavFragmentViewModel::class.java)
    }

    private fun initHomeViewModel(){
        homeFragmentViewModelFactory = HomeFragmentViewModelFactory(WeatherRepository)
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
        editor = sharedPreferences.edit()
        editor.putString("SelectedFragment","Fav")
        editor.apply()

    }

}