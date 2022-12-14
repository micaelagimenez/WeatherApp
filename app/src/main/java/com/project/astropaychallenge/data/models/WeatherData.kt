package com.project.astropaychallenge.data.models

data class WeatherData(
    var weather: List<Weather>,
    var main: Main,
    var wind: Wind,
    var name: String
)

data class Weather(
    var main: String,
    var description: String,
    var icon: String
)

data class Main(
    var temp: Float,
    var feels_like: Float,
    var temp_min: Float,
    var temp_max: Float,
    var pressure: Float,
    var humidity: Float
)

data class Wind(
    var speed: Float,
    var deg: Float,
    var gust: Float
)