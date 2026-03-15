package com.example.weatherapp.ui.utils

data class WeatherTheme(
    val bgImage: String,
    val houseImage: String,
    val iconImage: String
)

object WeatherMapper {
    fun getTheme(condition: String?, iconCode: String?): WeatherTheme {
        return when {
            // Thunderstorm
            iconCode?.startsWith("11") == true || condition?.contains("thunderstorm", ignoreCase = true) == true -> {
                WeatherTheme(
                    bgImage = "bg/storm.jpg",
                    houseImage = "houses/rain.png",
                    iconImage = "icons/storm.png"
                )
            }
            // Rain / Drizzle
            iconCode?.startsWith("09") == true || iconCode?.startsWith("10") == true || 
            condition?.contains("rain", ignoreCase = true) == true || condition?.contains("drizzle", ignoreCase = true) == true -> {
                WeatherTheme(
                    bgImage = "bg/rain2.jpg",
                    houseImage = "houses/rain.png",
                    iconImage = "icons/wind_rain.png"
                )
            }
            // Snow
            iconCode?.startsWith("13") == true || condition?.contains("snow", ignoreCase = true) == true -> {
                WeatherTheme(
                    bgImage = "bg/star2.jpg",
                    houseImage = "houses/morning_or_night.png",
                    iconImage = "icons/snaw.png"
                )
            }
            // Atmosphere (Mist, Smoke, etc.)
            iconCode?.startsWith("50") == true -> {
                WeatherTheme(
                    bgImage = "bg/storm_bar2.jpg",
                    houseImage = "houses/morning2.png",
                    iconImage = "icons/wind.png"
                )
            }
            // Clear sky
            iconCode == "01d" -> {
                WeatherTheme(
                    bgImage = "bg/star.jpg",
                    houseImage = "houses/morning2.png",
                    iconImage = "icons/sun.png"
                )
            }
            iconCode == "01n" -> {
                WeatherTheme(
                    bgImage = "bg/star.jpg",
                    houseImage = "houses/morning_or_night.png",
                    iconImage = "icons/full_moon.png"
                )
            }
            // Clouds
            iconCode?.startsWith("02") == true || iconCode?.startsWith("03") == true || iconCode?.startsWith("04") == true -> {
                val isNight = iconCode?.endsWith("n") == true
                WeatherTheme(
                    bgImage = if (isNight) "bg/star.jpg" else "bg/star2.jpg",
                    houseImage = if (isNight) "houses/morning_or_night.png" else "houses/morning2.png",
                    iconImage = if (isNight) "icons/scoudy_night.png" else "icons/cloudy_sun.png"
                )
            }
            // Default
            else -> {
                WeatherTheme(
                    bgImage = "bg/star.jpg",
                    houseImage = "houses/morning_or_night.png",
                    iconImage = "icons/sun.png"
                )
            }
        }
    }
}
