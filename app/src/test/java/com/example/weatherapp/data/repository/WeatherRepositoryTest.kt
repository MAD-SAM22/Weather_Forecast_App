package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.CurrentWeatherModel
import com.example.weatherapp.data.model.MainData
import com.example.weatherapp.data.model.Wind
import com.example.weatherapp.data.source.local.WeatherDao
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryTest {

    @MockK
    private lateinit var remoteDataSource: WeatherRemoteDataSource

    @MockK
    private lateinit var weatherDao: WeatherDao

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = WeatherRepository(
            remoteDataSource,
            weatherDao = weatherDao
        )
    }

    @Test
    fun getCurrentWeatherByCity_returnsSuccess() = runBlocking {
        val cityName = "Cairo"
        val weatherModel = CurrentWeatherModel(
            main = MainData(25.0, 24.0, 20.0, 30.0, 1012, 50),
            weather = emptyList(),
            wind = Wind(5.0, 180),
            name = cityName,
            dt = 1710000000L,
            timezone = 7200
        )
        val response = Response.success(weatherModel)

        coEvery { remoteDataSource.getCurrentWeatherByCity(cityName) } returns response

        val result = repository.getWeatherByCity(cityName)

        assertEquals(cityName, result.body()?.name)
        assertEquals(25.0, result.body()?.main?.temp)
    }
}
