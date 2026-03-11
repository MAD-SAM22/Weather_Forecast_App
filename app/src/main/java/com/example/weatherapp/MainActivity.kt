package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.home.HomeScreen
import com.example.weatherapp.ui.home.HomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // This is a simple way to provide the ViewModel without a DI framework for now
        val repository = WeatherRepository()
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(repository) as T
            }
        }

        setContent {
            WeatherAppTheme {
                val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
