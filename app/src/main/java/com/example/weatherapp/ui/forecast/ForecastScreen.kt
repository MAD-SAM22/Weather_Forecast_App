package com.example.weatherapp.ui.forecast

import androidx.compose.runtime.Composable
import com.example.weatherapp.ui.forecast.components.ForecastList

@Composable
fun ForecastScreen(viewModel: ForecastViewModel) {
    // UI implementation for Forecast Page
    ForecastList()
}
