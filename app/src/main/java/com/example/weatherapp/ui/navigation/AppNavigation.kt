package com.example.weatherapp.ui.navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.home.HomeScreen
import com.example.weatherapp.ui.home.HomeViewModel
import com.example.weatherapp.ui.onboarding.OnboardingScreen
import com.example.weatherapp.ui.onboarding.WeatherSplashScreen
import com.example.weatherapp.ui.lovedcities.LovedCitiesScreen
import org.koin.androidx.compose.koinViewModel

private const val PREFS_NAME = "weather_prefs"
private const val KEY_ONBOARDING_DONE = "onboarding_done"

private object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val LOVED_CITIES = "loved_cities"
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            WeatherSplashScreen {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val done = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
                val dest = if (done) Routes.HOME else Routes.ONBOARDING
                navController.navigate(dest) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen {
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit().putBoolean(KEY_ONBOARDING_DONE, true).apply()
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            }
        }

        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToCities = { navController.navigate(Routes.LOVED_CITIES) }
            )
        }

        composable(Routes.LOVED_CITIES) {
            LovedCitiesScreen(onBack = { navController.popBackStack() })
        }
    }
}
