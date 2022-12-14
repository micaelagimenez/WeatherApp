package com.project.astropaychallenge.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.astropaychallenge.data.datasources.Resource
import com.project.astropaychallenge.data.models.WeatherData
import com.project.astropaychallenge.data.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableLiveData<Resource<WeatherData>>()
    val weather: LiveData<Resource<WeatherData>> get() = _weather

    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.message?.let { Resource.error(it, exception) }
    }

    fun getWeather(country: String, appId: String) =
        viewModelScope.launch(Dispatchers.Main + handler) {
        _weather.value = Resource.loading()
        val response = withContext(Dispatchers.IO) {
            weatherRepository.getWeather(country, appId)
        }
        _weather.value = response
    }
}