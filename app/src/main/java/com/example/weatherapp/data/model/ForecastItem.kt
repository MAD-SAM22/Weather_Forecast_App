package com.example.weatherapp.data.model

data class ForecastItem(
    val time: String,
    val iconAssetPath: String,
    val temp: String,
    val isSelected: Boolean
)
