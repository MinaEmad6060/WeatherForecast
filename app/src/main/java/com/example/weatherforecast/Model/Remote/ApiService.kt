package com.example.weatherforecast.model.Remote

import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import com.example.weatherforecast.Model.Remote.Home.CurrentWeather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast")
    suspend fun getAdditionalWeatherAPI(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("cnt") cnt: Int
    ): CurrentWeather

//    @GET("https://api.openweathermap.org/data/3.0/onecall")
//    suspend fun getAlertWeatherAPI(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("appid") appid: String,
//    ): OneCallAlert
}


object ApiRetrofitHelper {
    private const val baseURL = "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()
}