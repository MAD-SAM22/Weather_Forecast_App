package com.example.weatherapp.di

import androidx.room.Room
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.source.local.WeatherDatabase
import com.example.weatherapp.data.source.local.SettingsManager
import com.example.weatherapp.ui.home.HomeViewModel
import com.example.weatherapp.ui.lovedcities.LovedCitiesViewModel
import com.example.weatherapp.ui.alerts.AlertsViewModel
import com.example.weatherapp.ui.alerts.AlarmTriggerViewModel
import com.example.weatherapp.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.weatherapp.data.source.remote.WeatherApiService
import com.example.weatherapp.data.source.remote.UnsplashApiService
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.source.remote.LocationHelper
import com.example.weatherapp.ui.utils.NetworkMonitor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named

val networkModule = module {
    single(named("WeatherRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    single(named("UnsplashRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>(named("WeatherRetrofit")).create(WeatherApiService::class.java) }
    single { get<Retrofit>(named("UnsplashRetrofit")).create(UnsplashApiService::class.java) }
    
    single { NetworkMonitor(androidContext()) }
}

val locationModule = module {
    single { LocationHelper(androidContext()) }
}

val settingsModule = module {
    single { SettingsManager(androidContext()) }
}

val repositoryModule = module {
    single<WeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get(), get() , get()) }
    single { WeatherRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { LovedCitiesViewModel(get(), get()) }
    viewModel { AlertsViewModel(androidApplication(), get(), get()) }
    viewModel { AlarmTriggerViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), WeatherDatabase::class.java, "weazy_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<WeatherDatabase>().weatherDao() }
}

val appModule = listOf(networkModule, locationModule, settingsModule, repositoryModule, viewModelModule, databaseModule)
