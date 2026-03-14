package com.example.weatherapp.di

import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.weatherapp.data.source.remote.WeatherApiService
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.source.remote.LocationHelper
import org.koin.android.ext.koin.androidContext

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(WeatherApiService::class.java) }
}

val locationModule = module {
    single { LocationHelper(androidContext()) }
}

val repositoryModule = module {
    single<WeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }
    single { WeatherRepository(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

val appModule = listOf(networkModule, locationModule, repositoryModule, viewModelModule)
