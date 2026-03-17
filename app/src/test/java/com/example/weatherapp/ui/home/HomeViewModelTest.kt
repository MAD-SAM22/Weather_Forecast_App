package com.example.weatherapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.model.CurrentWeatherModel
import com.example.weatherapp.data.model.MainData
import com.example.weatherapp.data.model.Wind
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.source.local.SettingsManager
import com.example.weatherapp.data.source.local.WeatherDao
import com.example.weatherapp.data.source.remote.LocationHelper
import com.example.weatherapp.ui.utils.NetworkMonitor
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: WeatherRepository

    @MockK
    private lateinit var locationHelper: LocationHelper

    @MockK
    private lateinit var weatherDao: WeatherDao

    @MockK
    private lateinit var settingsManager: SettingsManager

    @MockK
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        every { settingsManager.tempUnits } returns MutableStateFlow("metric")
        every { settingsManager.windSpeedUnits } returns MutableStateFlow("m/s")
        every { settingsManager.language } returns MutableStateFlow("en")
        
        // Mock network monitor and dao flows used in init
        every { networkMonitor.isOnline } returns flowOf(true)
        every { weatherDao.getCurrentWeather() } returns flowOf(null)

        viewModel = HomeViewModel(
            repository, locationHelper, weatherDao, settingsManager, networkMonitor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchWeatherData_updatesWeatherState() = runTest {
        val cityName = "London"
        val weatherModel = CurrentWeatherModel(
            main = MainData(15.0, 14.0, 10.0, 20.0, 1012, 70),
            weather = emptyList(),
            wind = Wind(3.0, 90),
            name = cityName,
            dt = 1710000000L,
            timezone = 0
        )
        coEvery { repository.getWeatherByCity(cityName) } returns Response.success(weatherModel)
        coEvery { repository.getForecastByCity(cityName) } returns Response.success(null)

        viewModel.fetchWeatherData(cityName)

        assertEquals(cityName, viewModel.weatherState?.name)
        assertEquals(15.0, viewModel.weatherState?.main?.temp)
    }
}
