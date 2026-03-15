package com.example.weatherapp.ui.utils

import java.util.Calendar
import java.util.TimeZone

data class WeatherTheme(
    val bgImage: String,
    val houseImage: String,
    val iconImage: String
)

object WeatherMapper {
    fun getTheme(condition: String?, iconCode: String?, timestamp: Long? = null, timezoneOffset: Int? = null): WeatherTheme {
        val calendar = if (timezoneOffset != null) {
            val tzId = TimeZone.getAvailableIDs(timezoneOffset * 1000).firstOrNull() ?: "UTC"
            Calendar.getInstance(TimeZone.getTimeZone(tzId))
        } else {
            Calendar.getInstance()
        }

        if (timestamp != null) {
            calendar.timeInMillis = timestamp * 1000
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val isNight = iconCode?.endsWith("n") == true || hour !in 6..18

        return when {
            // Thunderstorm
            iconCode?.startsWith("11") == true || condition?.contains("thunderstorm", ignoreCase = true) == true -> {
                val icon = if (condition?.contains("rain", ignoreCase = true) == true) "icons/bar2_wind_rain.png" else "icons/bar2.png"
                WeatherTheme(
                    bgImage = "bg/storm.jpg",
                    houseImage = "houses/rain.png",
                    iconImage = icon
                )
            }
            // Rain / Drizzle
            iconCode?.startsWith("09") == true || iconCode?.startsWith("10") == true || 
            condition?.contains("rain", ignoreCase = true) == true || condition?.contains("drizzle", ignoreCase = true) == true -> {
                val icon = if (condition?.contains("wind", ignoreCase = true) == true) {
                    "icons/wind_rain.png"
                } else if (isNight) {
                    "icons/water_drops_night.png"
                } else {
                    "icons/water_drops_sun.png"
                }
                
                WeatherTheme(
                    bgImage = if (isNight) "bg/rain2.jpg" else "bg/rain1.jpg",
                    houseImage = "houses/rain.png",
                    iconImage = icon
                )
            }
            // Snow
            iconCode?.startsWith("13") == true || condition?.contains("snow", ignoreCase = true) == true -> {
                WeatherTheme(
                    bgImage = if (isNight) "bg/star2.jpg" else "bg/star.jpg",
                    houseImage = "houses/snow.png",
                    iconImage = if (isNight) "icons/night_snow.png" else "icons/snaw.png"
                )
            }
            // Atmosphere (Mist, Smoke, etc.)
            iconCode?.startsWith("50") == true -> {
                val icon = if (condition?.contains("tornado", ignoreCase = true) == true || condition?.contains("squall", ignoreCase = true) == true) {
                    "icons/storm.png"
                } else {
                    "icons/wind.png"
                }
                WeatherTheme(
                    bgImage = "bg/storm_bar2.jpg",
                    houseImage = if (isNight) "houses/morning_or_night.png" else "houses/morning2.png",
                    iconImage = icon
                )
            }
            // Clear sky or Clouds
            else -> {
                if (iconCode?.startsWith("01") == true) { // Clear
                    val icon = if (condition?.contains("wind", ignoreCase = true) == true && !isNight) {
                        "icons/sun_wind.png"
                    } else if (isNight) {
                        "icons/full_moon.png"
                    } else {
                        "icons/sun.png"
                    }

                    when {
                        hour in 6..10 -> WeatherTheme("bg/morning.jpg", "houses/morning.png", icon)
                        hour in 11..16 -> WeatherTheme("bg/midday.jpg", "houses/morning2.png", icon)
                        hour in 17..18 -> WeatherTheme("bg/morning2.jpg", "houses/morning3.png", icon)
                        else -> WeatherTheme(if (hour in 19..22) "bg/night.jpg" else "bg/night2.jpg", "houses/night.png", icon)
                    }
                } else { // Clouds
                    when {
                        hour in 6..18 -> {
                            WeatherTheme(
                                bgImage = if (hour in 11..16) "bg/midday.jpg" else "bg/morning.jpg",
                                houseImage = "houses/morning2.png",
                                iconImage = "icons/cloudy_sun.png"
                            )
                        }
                        else -> {
                            WeatherTheme(
                                bgImage = if (hour in 19..22) "bg/night.jpg" else "bg/night2.jpg",
                                houseImage = "houses/night.png",
                                iconImage = "icons/scoudy_night.png"
                            )
                        }
                    }
                }
            }
        }
    }
}
