package com.example.weatherforecast.Favourite.View

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecast.Model.CurrentWeather
import com.example.weatherforecast.R

class FavouriteFragmentAdapter :
    ListAdapter<CurrentWeather, FavouriteWeatherViewHolder>(FavWeatherDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteWeatherViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_fav,parent,false)
        return FavouriteWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavouriteWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
        holder.favCity.text = currentObj.name
        holder.favTemp.text = currentObj.main.temp.toString()
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/"
                    +currentObj.weather[0].icon+"@2x.png")
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.favImg)
    }
}


class FavouriteWeatherViewHolder (view : View): RecyclerView.ViewHolder(view){
    var favCity : TextView = view.findViewById(R.id.fav_weather_city)
    var favImg : ImageView = view.findViewById(R.id.fav_weather_img)
    var favTemp : TextView = view.findViewById(R.id.fav_weather_temp)
}


