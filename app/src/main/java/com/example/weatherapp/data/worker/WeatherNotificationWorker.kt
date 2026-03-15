package com.example.weatherapp.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import com.example.weatherapp.data.repository.WeatherRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WeatherNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val repository: WeatherRepository by inject()

    override suspend fun doWork(): Result {
        val cityName = inputData.getString("city_name") ?: return Result.failure()
        val alertType = inputData.getString("alert_type") ?: "Notification"

        // Fetch current weather for the alert
        val response = repository.getWeatherByCity(cityName)
        if (response.isSuccessful) {
            val weather = response.body()
            if (weather != null) {
                showNotification(
                    cityName,
                    "Current temperature in $cityName is ${weather.main.temp.toInt()}°C. ${weather.weather.firstOrNull()?.description ?: ""}",
                    alertType == "Alarm"
                )
            }
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String, isAlarm: Boolean) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "weather_alerts_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weather Alerts",
                if (isAlarm) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for weather alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(if (isAlarm) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
