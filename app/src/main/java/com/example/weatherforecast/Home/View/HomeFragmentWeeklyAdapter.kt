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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecast.Model.Remote.AdditionalWeather
import com.example.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragmentWeeklyAdapter :
    ListAdapter<AdditionalWeather, WeeklyWeatherViewHolder>(WeatherDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyWeatherViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_weekly,parent,false)
        return WeeklyWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeeklyWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
        if (position==0){
            holder.weeklyDay.text = "Tomorrow"
        }else{
            holder.weeklyDay.text = convertDateFormat(currentObj.dt_txt.split(" ")[0])
        }
            holder.weeklyDesc.text = currentObj.weather[0].description
            holder.weeklyDate.text = currentObj.dt_txt.split(" ")[0]
            holder.weeklyTemp.text =
                currentObj.main.temp_min.toInt().toString()+"/"+
                currentObj.main.temp_max.toInt().toString()+"°C"

            Glide.with(holder.itemView.context)
                .load("https://openweathermap.org/img/wn/"
                        +currentObj.weather[0].icon+"@2x.png")
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.weeklyImg)

    }
}


class WeeklyWeatherViewHolder (view : View): RecyclerView.ViewHolder(view){
    val weeklyDay : TextView= view.findViewById(R.id.additional_weather_day_weekly)
    var weeklyDesc : TextView= view.findViewById(R.id.additional_weather_desc_weekly)
    var weeklyImg : ImageView= view.findViewById(R.id.additional_weather_img_weekly)
    var weeklyDate : TextView= view.findViewById(R.id.additional_weather_date_weekly)
    var weeklyTemp : TextView= view.findViewById(R.id.additional_weather_temp_weekly)
}



fun convertDateFormat(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val newDate = inputFormat.parse(date)
    return outputFormat.format(newDate)
}
