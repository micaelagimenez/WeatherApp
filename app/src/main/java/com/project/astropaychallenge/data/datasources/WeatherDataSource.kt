package com.project.astropaychallenge.data.datasources

import com.project.astropaychallenge.data.services.WeatherService
import javax.inject.Inject

class WeatherDataSource @Inject constructor(private val weatherService: WeatherService) : BaseDataSource() {

    suspend fun getWeather(country: String, appId: String) =
        getResult { weatherService.getWeatherResponse(country, appId) }
}