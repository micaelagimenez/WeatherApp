package com.project.astropaychallenge.data.repositories

import com.project.astropaychallenge.data.datasources.WeatherDataSource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remote: WeatherDataSource
) {
    suspend fun getWeather(country: String, appId: String) =
        remote.getWeather(country, appId)
}