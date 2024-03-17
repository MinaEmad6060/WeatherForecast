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
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.MapsActivity
import com.example.weatherforecast.Model.DataState
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
    private lateinit var favFragmentViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var favFragmentViewModel: HomeFragmentViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favFab.setOnClickListener {
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }

        handlingFavFAB()
        setFavAdapter()
        getSharedPreferences()
        initViewModel()

        favFragmentViewModel.getWeatherRemoteVM(
            lat, lon, "a92ea15347fafa48d308e4c367a39bb8", "metric", "en"
        )



        lifecycleScope.launch {
            favFragmentViewModel.weatherList.collectLatest { value ->
                when(value){
                    is DataState.Success -> {
                        Log.i(TAG, "additionalWeatherList-success: $lat $lon")
//                        for (i in hourlyList){
//                            Log.i(TAG, "print element: ${i.dt_txt}")
//                        }
                        //favAdapter.submitList(value.data)
                    }
                    is DataState.Failure -> {Log.i(TAG, "additionalWeatherList-fail: ")}
                    else -> Log.i(TAG, "loading: ")
                }
            }
        }

    }

    private fun handlingFavFAB(){
        binding.favFab.setOnClickListener {
            editor = sharedPreferences.edit()
            editor.putString("SelectedFragment", "Fav")
            editor.apply()
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }
    }

    private fun setFavAdapter(){
        favAdapter = FavouriteFragmentAdapter()
        favLayoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.HORIZONTAL, false)
        binding.favRecyclerView.apply {
            adapter = favAdapter
            layoutManager = favLayoutManager
        }
        //favAdapter.submitList(listOfWeather)
    }

    private fun initViewModel(){
        favFragmentViewModelFactory = HomeFragmentViewModelFactory(WeatherRepository)
        favFragmentViewModel =
            ViewModelProvider(this, favFragmentViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

    private fun getSharedPreferences()
    {
        sharedPreferences =
            requireActivity().getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        lat = sharedPreferences.getString("latitude", "0")!!.toDouble()
        lon = sharedPreferences.getString("longitude", "0")!!.toDouble()
    }

}