package com.example.weatherapp.ui.settings

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.source.local.SettingsManager
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel() {
    val language: StateFlow<String> = settingsManager.language
    val tempUnits: StateFlow<String> = settingsManager.tempUnits
    val windUnits: StateFlow<String> = settingsManager.windSpeedUnits
    
    // Alias for compatibility with SettingsScreen
    val units: StateFlow<String> = settingsManager.tempUnits

    fun setLanguage(lang: String) {
        settingsManager.setLanguage(lang)
    }

    fun setTempUnits(unit: String) {
        settingsManager.setTempUnits(unit)
    }

    fun setWindSpeedUnits(unit: String) {
        settingsManager.setWindSpeedUnits(unit)
    }
    
    // Alias for compatibility with SettingsScreen
    fun setUnits(unit: String) {
        settingsManager.setTempUnits(unit)
    }
}
