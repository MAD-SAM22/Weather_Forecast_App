package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName
data class CurrentWeatherModel(
    val main: MainData,
    val weather: List<WeatherDescription>,
    val wind: Wind,
    val name: String, // City Name
    val dt: Long,     // Timestamp
    val timezone: Int, // Timezone offset in seconds from UTC
    val sys: SysData? = null,
    val rain: RainData? = null
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

data class SysData(
    val type: Int? = null,
    val id: Int? = null,
    val country: String? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null
)

data class RainData(

    @SerializedName("1h")
    val oneHour: Double? = null,
    @SerializedName("3h")
    val threeHour: Double? = null
)