package com.example.weatherforecast.Home.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecast.Model.Remote.DetailedWeather
import com.example.weatherforecast.R


class HomeFragmentAdapter(private val selectLayout: Int) :
    ListAdapter<DetailedWeather, ProductViewHolder>(WeatherDiffUtil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(selectLayout,parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentObj = getItem(position)
//        holder.name.text = "0"
//        Glide.with(holder.itemView.context)
//            .load(currentObj.thumbnail)
//            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(holder.thumbnail)
//        }
    }

}

class ProductViewHolder (view : View): RecyclerView.ViewHolder(view){
//    var thumbnail : ImageView = view.findViewById(R.id.product_img)
//    var name : TextView = view.findViewById(R.id.weather_temp_item)
//    val btnAdd : Button = view.findViewById(R.id.btn_add)
}
