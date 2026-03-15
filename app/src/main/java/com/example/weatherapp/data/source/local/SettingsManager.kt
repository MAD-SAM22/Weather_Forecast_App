package com.example.weatherapp.data.source.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("weazy_settings", Context.MODE_PRIVATE)

    private val _language = MutableStateFlow(prefs.getString("language", "en") ?: "en")
    val language: StateFlow<String> = _language

    private val _tempUnits = MutableStateFlow(prefs.getString("temp_units", "metric") ?: "metric")
    val tempUnits: StateFlow<String> = _tempUnits

    private val _windSpeedUnits = MutableStateFlow(prefs.getString("wind_units", "m/s") ?: "m/s")
    val windSpeedUnits: StateFlow<String> = _windSpeedUnits

    fun setLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
        _language.value = lang
    }

    fun setTempUnits(unit: String) {
        prefs.edit().putString("temp_units", unit).apply()
        _tempUnits.value = unit
    }

    fun setWindSpeedUnits(unit: String) {
        prefs.edit().putString("wind_units", unit).apply()
        _windSpeedUnits.value = unit
    }
}
