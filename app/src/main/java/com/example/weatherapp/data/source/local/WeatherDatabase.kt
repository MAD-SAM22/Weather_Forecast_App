package com.example.weatherapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.source.local.entity.CurrentWeatherEntity
import com.example.weatherapp.data.source.local.entity.FavoriteCityEntity
import com.example.weatherapp.data.source.local.entity.WeatherAlertEntity

@Database(
    entities = [
        CurrentWeatherEntity::class,
        FavoriteCityEntity::class,
        WeatherAlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
