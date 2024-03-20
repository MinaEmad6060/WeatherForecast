package com.example.weatherforecast.Favourite.View

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Remote.CurrentWeather
import com.example.weatherforecast.R

class FavouriteFragmentAdapter :
    ListAdapter<FavWeather, FavouriteWeatherViewHolder>(FavWeatherDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteWeatherViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_fav,parent,false)
        return FavouriteWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavouriteWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
        Log.i("adapterFav", "currentObj :${currentObj.cityName}")
        holder.favCity.text = currentObj.cityName
        holder.favTemp.text = currentObj.temperature.toInt().toString()+"°C"
    }
}


class FavouriteWeatherViewHolder (view : View): RecyclerView.ViewHolder(view){
    var favCity : TextView = view.findViewById(R.id.fav_weather_city)
    var favTemp : TextView = view.findViewById(R.id.fav_weather_temp)
}


