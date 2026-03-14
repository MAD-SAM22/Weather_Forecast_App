package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.model.CurrentWeatherModel
import com.example.weatherapp.data.model.ForecastResponse
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeatherByCoords(lat: Double, lon: Double): Response<CurrentWeatherModel>
    suspend fun getCurrentWeatherByCity(city: String): Response<CurrentWeatherModel>
    suspend fun getForecastByCoords(lat: Double, lon: Double): Response<ForecastResponse>
    suspend fun getForecastByCity(city: String): Response<ForecastResponse>
}

class WeatherRemoteDataSourceImpl(private val apiService: WeatherApiService) : WeatherRemoteDataSource {

    private val API_KEY = "037a373011c520cc888756c98e9d9260"

    override suspend fun getCurrentWeatherByCoords(lat: Double, lon: Double): Response<CurrentWeatherModel> {
        return apiService.getCurrentWeather(lat = lat, lon = lon, apiKey = API_KEY)
    }

    override suspend fun getCurrentWeatherByCity(city: String): Response<CurrentWeatherModel> {
        return apiService.getCurrentWeather(cityName = city, apiKey = API_KEY)
    }

    override suspend fun getForecastByCoords(lat: Double, lon: Double): Response<ForecastResponse> {
        return apiService.getFiveDayForecast(lat = lat, lon = lon, apiKey = API_KEY)
    }

    override suspend fun getForecastByCity(city: String): Response<ForecastResponse> {
        return apiService.getFiveDayForecast(cityName = city, apiKey = API_KEY)
    }
}