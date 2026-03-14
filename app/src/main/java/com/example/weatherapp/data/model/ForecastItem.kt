package com.example.weatherapp.data.model

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: CityInfo
)

data class ForecastItem(
    val dt: Long,                 // Time of data forecasted
    val main: MainData,           // Reusing the MainData class from above
    val weather: List<WeatherDescription>,
    val dt_txt: String            // Date/Time in string format (e.g., "2026-03-13 18:00:00")
)

data class CityInfo(
    val name: String,
    val country: String,
    val population: Int,
    val timezone: Int
)

data class ForecastDisplayItem(
    val time: String,
    val iconAssetPath: String,
    val temp: String,
    val isSelected: Boolean = false
)
