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
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: WeatherRepository

    @Mock
    private lateinit var locationHelper: LocationHelper

    @Mock
    private lateinit var weatherDao: WeatherDao

    @Mock
    private lateinit var settingsManager: SettingsManager

    @Mock
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Mock settings manager flows
        `when`(settingsManager.tempUnits).thenReturn(MutableStateFlow("metric"))
        `when`(settingsManager.windSpeedUnits).thenReturn(MutableStateFlow("m/s"))
        `when`(settingsManager.language).thenReturn(MutableStateFlow("en"))
        
        // Mock network monitor and dao flows used in init
        `when`(networkMonitor.isOnline).thenReturn(flowOf(true))
        `when`(weatherDao.getCurrentWeather()).thenReturn(flowOf(null))

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
        `when`(repository.getWeatherByCity(cityName)).thenReturn(Response.success(weatherModel))
        `when`(repository.getForecastByCity(cityName)).thenReturn(Response.success(null))

        viewModel.fetchWeatherData(cityName)

        assertEquals(cityName, viewModel.weatherState?.name)
        assertEquals(15.0, viewModel.weatherState?.main?.temp)
    }
}
