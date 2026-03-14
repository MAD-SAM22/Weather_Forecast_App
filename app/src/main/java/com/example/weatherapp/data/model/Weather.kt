package com.example.weatherapp.data.model

data class CurrentWeatherModel(
    val main: MainData,
    val weather: List<WeatherDescription>,
    val wind: Wind,
    val name: String, // City Name
    val dt: Long     // Timestamp
)

data class MainData(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String // e.g., "01d"
)

data class Wind(
    val speed: Double,
    val deg: Int
)