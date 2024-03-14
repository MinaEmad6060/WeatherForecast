package com.example.weatherforecast.Model.Remote

import com.example.weatherforecast.Model.CurrentWeather

class WeatherRemoteDataSource:InterWeatherRemoteDataSource {
    override suspend fun getAllWeatherRemote(): CurrentWeather {
        return API.retrofitService.getWeatherAPI(31.26863,30.0059383,"a92ea15347fafa48d308e4c367a39bb8")
    }
}