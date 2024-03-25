package com.example.weatherforecast.Alert.View

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.R

class AlertFragmentAdapter(val deleteAlertCalendar: (AlertCalendar)->Int)
    : ListAdapter<AlertCalendar, AlertWeatherViewHolder>(AlertWeatherDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertWeatherViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_alert,parent,false)
        return AlertWeatherViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlertWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
        holder.startDate.text=currentObj.infoOfAlert.split(",")[0]
        holder.startTime.text=currentObj.infoOfAlert.split(",")[1]
        holder.endDate.text=currentObj.infoOfAlert.split(",")[2]
        holder.endTime.text=currentObj.infoOfAlert.split(",")[3]

        holder.btnDel.setOnClickListener{ deleteAlertCalendar(currentObj) }
    }
}


class AlertWeatherViewHolder (view : View): RecyclerView.ViewHolder(view){
    var startDate: TextView = view.findViewById(R.id.alert_start_date)
    var endDate: TextView = view.findViewById(R.id.alert_end_date)
    var startTime: TextView = view.findViewById(R.id.alert_start_time)
    var endTime: TextView = view.findViewById(R.id.alert_end_time)

    var btnDel : ImageButton = view.findViewById(R.id.alert_delete)

}


