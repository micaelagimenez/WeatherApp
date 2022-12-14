package com.project.astropaychallenge.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.project.astropaychallenge.BuildConfig
import com.project.astropaychallenge.R
import com.project.astropaychallenge.data.datasources.Resource
import com.project.astropaychallenge.databinding.ActivityMainBinding
import com.project.astropaychallenge.ui.viewmodels.WeatherViewModel
import com.project.astropaychallenge.utils.parseKelvinToCelsius
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    var longitude = 0.0
    var latitude = 0.0
    var buttonClickCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get weather from user's GPS
        binding.ibGetLocation.setOnClickListener {
            buttonClickCounter += 1
            initiateGps()
        }

        // todo pass region
        weatherViewModel.getWeather("London", BuildConfig.API_KEY)

        weatherViewModel.weather.observe(this) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.isVisible = true
                    binding.weatherError.root.isVisible = false
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.isVisible = false
                    binding.weatherError.root.isVisible = false

                    // bind data
                    if (it.data != null) {
                        for (i in it.data.weather) {
                            binding.tvDescription.text = i.main
                            parseIconData(i.icon)
                        }
                        binding.tvTemperature.text = parseKelvinToCelsius(it.data.main.temp)
                        binding.tvMaxTemperature.text =
                            resources.getString(R.string.tv_max_temp) + parseKelvinToCelsius(it.data.main.temp_max)
                        binding.tvMinTemperature.text =
                            resources.getString(R.string.tv_min_temp) + parseKelvinToCelsius(it.data.main.temp_min)
                        binding.tvWind.text =
                            resources.getString(R.string.tv_wind) + it.data.wind.speed.toString()
                        binding.tvLocation.text = it.data.name
                    }
                }

                Resource.Status.ERROR -> {
                    binding.progressBar.isVisible = false
                    binding.weatherError.root.isVisible = true
                    binding.weatherError.btnRetry.setOnClickListener {
                        weatherViewModel.getWeather("London", BuildConfig.API_KEY)
                        binding.progressBar.isVisible = false
                    }
                }
            }
        }
    }

    private fun parseIconData(iconCode: String) {
        val icon = "https://openweathermap.org/img/wn/$iconCode@4x.png"
        Picasso.get().load(icon)
            .into(binding.ivWeather)
    }

    private fun initiateGps() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if permissions are already granted
        if (checkIfPermissionsAreGranted()) {
            if (buttonClickCounter == 1) showGpsRequestDialog()
        } else {
            // Ask for permissions if they weren't granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ),
                1
            )
            if (buttonClickCounter == 1) showGpsRequestDialog()
        }

        // Get latitude and longitude
        val criteria = Criteria()
        val provider = locationManager.getBestProvider(criteria, false)
        if (provider != null) {
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            }

            // Get city name
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    val city = addresses[0].locality
                    weatherViewModel.getWeather(city, BuildConfig.API_KEY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun checkIfPermissionsAreGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun showGpsRequestDialog() {
        AlertDialog.Builder(this)
            .setTitle("Activate GPS")
            .setMessage("Please make sure to activate your phone's GPS to get your location data and click on the button again")
            .setPositiveButton(resources.getString(R.string.dialog_button), null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}