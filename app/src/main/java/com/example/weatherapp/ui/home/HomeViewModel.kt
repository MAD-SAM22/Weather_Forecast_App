package com.example.weatherapp.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.CurrentWeatherModel
import com.example.weatherapp.data.model.ForecastDisplayItem
import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.source.remote.LocationHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(
    private val repository: WeatherRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    var weatherState by mutableStateOf<CurrentWeatherModel?>(null)
        private set

    var hourlyForecast by mutableStateOf<List<ForecastDisplayItem>>(emptyList())
        private set

    var weeklyForecast by mutableStateOf<List<ForecastDisplayItem>>(emptyList())
        private set

    var isHourlySelected by mutableStateOf(true)
        private set

    init {
        loadWeatherForCurrentLocation()
    }

    fun loadWeatherForCurrentLocation() {
        Log.d("HomeViewModel", "Loading weather for current location...")
        locationHelper.getDeviceLocation { lat, lon ->
            Log.d("HomeViewModel", "Received location: $lat, $lon")
            fetchWeatherData(lat, lon)
        }
        
        // Safety timeout/fallback: if after 5 seconds we still have no data, load a default city
        viewModelScope.launch {
            kotlinx.coroutines.delay(5000)
            if (weatherState == null) {
                Log.d("HomeViewModel", "Location timeout, falling back to default city")
                fetchWeatherData("Egypt")
            }
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val weatherResponse = repository.getCurrentWeatherByCoords(lat, lon)
                if (weatherResponse.isSuccessful) {
                    weatherState = weatherResponse.body()
                }

                val forecastResponse = repository.getForecastByCoords(lat, lon)
                if (forecastResponse.isSuccessful) {
                    processForecast(forecastResponse.body())
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching weather by coords", e)
                fetchWeatherData("Egypt") // Fallback on error
            }
        }
    }

    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            try {
                val weatherResponse = repository.getWeatherByCity(city)
                if (weatherResponse.isSuccessful) {
                    weatherState = weatherResponse.body()
                }

                val forecastResponse = repository.getForecastByCity(city)
                if (forecastResponse.isSuccessful) {
                    processForecast(forecastResponse.body())
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching weather by city", e)
            }
        }
    }

    private fun processForecast(forecast: ForecastResponse?) {
        forecast?.let {
            val allItems = it.list
            
            // Hourly Forecast (next 8 items, 24 hours)
            hourlyForecast = allItems.take(8).map { item ->
                ForecastDisplayItem(
                    time = formatTime(item.dt),
                    iconAssetPath = mapIconToAsset(item.weather.firstOrNull()?.icon),
                    temp = "${item.main.temp.toInt()}°"
                )
            }

            // Weekly Forecast (one item per day)
            weeklyForecast = allItems.filterIndexed { index, _ -> index % 8 == 0 }.take(5).map { item ->
                ForecastDisplayItem(
                    time = formatDate(item.dt),
                    iconAssetPath = mapIconToAsset(item.weather.firstOrNull()?.icon),
                    temp = "${item.main.temp.toInt()}°"
                )
            }
        }
    }

    fun toggleForecastType(isHourly: Boolean) {
        isHourlySelected = isHourly
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("h a", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    private fun mapIconToAsset(icon: String?): String {
        return when (icon) {
            "01d", "01n" -> "icons/sun.png"
            "02d", "02n", "03d", "03n", "04d", "04n" -> "icons/scoudy_night.png"
            "09d", "09n", "10d", "10n" -> "icons/rain.png"
            else -> "icons/water_drops_night.png"
        }
    }
}
