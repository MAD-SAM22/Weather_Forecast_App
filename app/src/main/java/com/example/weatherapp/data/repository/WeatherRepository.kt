package com.example.weatherapp.data.repository

import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.model.CurrentWeatherModel
import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.data.model.GeocodingResponseItem
import com.example.weatherapp.data.model.UnsplashResponse
import com.example.weatherapp.data.source.local.WeatherDao
import com.example.weatherapp.data.source.local.entity.CurrentWeatherEntity
import retrofit2.Response

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val weatherDao: WeatherDao
) {

    suspend fun getCurrentWeatherByCoords(lat: Double, lon: Double): Response<CurrentWeatherModel> {
        val response = remoteDataSource.getCurrentWeatherByCoords(lat, lon)
        if (response.isSuccessful) {
            response.body()?.let { model ->
                weatherDao.insertCurrentWeather(
                    CurrentWeatherEntity(
                        cityName = model.name,
                        temp = model.main.temp,
                        tempMax = model.main.temp_max,
                        tempMin = model.main.temp_min,
                        condition = model.weather.firstOrNull()?.description ?: "",
                        icon = model.weather.firstOrNull()?.icon ?: "",
                        timestamp = model.dt,
                        windSpeed = model.wind.speed,
                        windDeg = model.wind.deg,
                        sunrise = model.sys?.sunrise,
                        sunset = model.sys?.sunset,
                        rainVolume = model.rain?.oneHour ?: model.rain?.threeHour
                    )
                )
            }
        }
        return response
    }

    suspend fun getForecastByCoords(lat: Double, lon: Double): Response<ForecastResponse> {
        return remoteDataSource.getForecastByCoords(lat, lon)
    }

    suspend fun getWeatherByCity(city: String): Response<CurrentWeatherModel> {
        val response = remoteDataSource.getCurrentWeatherByCity(city)
        if (response.isSuccessful) {
            response.body()?.let { model ->
                weatherDao.insertCurrentWeather(
                    CurrentWeatherEntity(
                        cityName = model.name,
                        temp = model.main.temp,
                        tempMax = model.main.temp_max,
                        tempMin = model.main.temp_min,
                        condition = model.weather.firstOrNull()?.description ?: "",
                        icon = model.weather.firstOrNull()?.icon ?: "",
                        timestamp = model.dt,
                        windSpeed = model.wind.speed,
                        windDeg = model.wind.deg,
                        sunrise = model.sys?.sunrise,
                        sunset = model.sys?.sunset,
                        rainVolume = model.rain?.oneHour ?: model.rain?.threeHour
                    )
                )
            }
        }
        return response
    }

    suspend fun getForecastByCity(city: String): Response<ForecastResponse>{
        return remoteDataSource.getForecastByCity(city)
    }

    suspend fun getCityImage(city: String): Response<UnsplashResponse> {
        return remoteDataSource.getCityImage(city)
    }

    suspend fun searchCity(query: String): Response<List<GeocodingResponseItem>> {
        return remoteDataSource.searchCity(query)
    }

    suspend fun reverseGeocode(lat: Double, lon: Double): Response<List<GeocodingResponseItem>> {
        return remoteDataSource.reverseGeocode(lat, lon)
    }
}
