package com.project.astropaychallenge.data.services

import com.project.astropaychallenge.data.models.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/data/2.5/weather")
    suspend fun getWeatherResponse(@Query("q") country: String, @Query("appid") appId: String):
            Response<WeatherData>
}