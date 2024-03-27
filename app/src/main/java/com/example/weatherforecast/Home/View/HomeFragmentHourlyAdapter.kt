package com.example.weatherforecast.Home.View

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
import com.example.weatherforecast.Model.Remote.Home.AdditionalWeather
import com.example.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragmentHourlyAdapter :
    ListAdapter<AdditionalWeather, HourlyWeatherViewHolder>(WeatherDiffUtil()){
        var units: String="°C"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_hourly,parent,false)
        return HourlyWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
            holder.hourlyTime.text = convertTimeFormat(currentObj.dt_txt.split(" ")[1])
            holder.hourlyTemp.text = currentObj.main.temp.toInt().toString()
            if (position==0){
                units=currentObj.units
            }
            holder.hourlyUnit.text = units
            Glide.with(holder.itemView.context)
                .load("https://openweathermap.org/img/wn/"
                        +currentObj.weather[0].icon+"@2x.png")
                .into(holder.hourlyImg)
    }
}


class HourlyWeatherViewHolder (view : View): RecyclerView.ViewHolder(view){
    var hourlyTime : TextView = view.findViewById(R.id.additional_weather_time_hourly)
    var hourlyImg : ImageView = view.findViewById(R.id.additional_weather_img_hourly)
    var hourlyTemp : TextView = view.findViewById(R.id.additional_weather_temp_hourly)
    var hourlyUnit : TextView = view.findViewById(R.id.temperature_unit_hourly)
}



fun convertTimeFormat(time: String): String{
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h a", Locale.getDefault())
    val date = inputFormat.parse(time)
    return outputFormat.format(date)
}



