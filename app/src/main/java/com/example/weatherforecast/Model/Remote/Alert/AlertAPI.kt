package com.example.weatherforecast.Model.Remote.Alert

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AlertAPI {

    @GET("onecall")
    suspend fun getAlertWeatherAPI(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
    ): OneCallAlert
}


object RetrofitHelper {
    private const val baseURL = "https://api.openweathermap.org/data/3.0/"
    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()
}
//object API {
//    val retrofitAlertService : AlertAPI by lazy {
//        RetrofitHelper.retrofitInstance.create(AlertAPI::class.java)
//    }
//}