package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.CurrentWeatherModel
import com.example.weatherapp.data.model.MainData
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class WeatherRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: WeatherRemoteDataSource

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepository(remoteDataSource)
    }

    @Test
    fun getCurrentWeatherByCity_returnsSuccess() = runBlocking {
        val cityName = "Cairo"
        val weatherModel = CurrentWeatherModel(
            main = MainData(25.0, 24.0, 20.0, 30.0, 1012, 50),
            weather = emptyList(),
            wind = com.example.weatherapp.data.model.Wind(5.0, 180),
            name = cityName,
            dt = 1710000000L,
            timezone = 7200
        )
        val response = Response.success(weatherModel)

        `when`(remoteDataSource.getCurrentWeatherByCity(cityName)).thenReturn(response)

        val result = repository.getWeatherByCity(cityName)

        assertEquals(cityName, result.body()?.name)
        assertEquals(25.0, result.body()?.main?.temp)
    }
}
