package com.example.weatherapp.ui.weather

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: Any) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
