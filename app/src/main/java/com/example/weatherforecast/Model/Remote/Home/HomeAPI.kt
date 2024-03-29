package com.example.weatherforecast.Model.Remote.Home

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface HomeAPI {

    @GET("forecast")
    suspend fun getAdditionalWeatherAPI(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("cnt") cnt: Int
    ): CurrentWeather
}

/*https://api.openweathermap.org/data/2.5/weather?
lat=31.26863&lon=30.0059383&appid=a92ea15347fafa48d308e4c367a39bb8&units=metric*/

object HomeRetrofitHelper {
    private const val baseURL = "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()
}