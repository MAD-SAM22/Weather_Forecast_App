package com.example.weatherapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.model.CurrentWeatherModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun WeatherDetailsSection(weather: CurrentWeatherModel?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Air Quality
        DetailCard(
            title = "AIR QUALITY",
            mainText = "3-Low Health Risk",
            footerText = "See more",
            hasProgressBar = true,
            progress = 0.3f,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // UV Index
            DetailCard(
                title = "UV INDEX",
                mainText = "4",
                secondaryText = "Moderate",
                progress = 0.4f,
                hasProgressBar = true,
                modifier = Modifier.weight(1f)
            )

            // Sunrise
            DetailCard(
                title = "SUNRISE",
                mainText = weather?.sys?.sunrise?.let { formatTime(it, weather.timezone) } ?: "5:28 AM",
                secondaryText = "Sunset: ${weather?.sys?.sunset?.let { formatTime(it, weather.timezone) } ?: "7:25 PM"}",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Wind
            DetailCard(
                title = "WIND",
                mainText = "${weather?.wind?.speed?.toInt() ?: 9} km/h",
                secondaryText = "Direction: ${weather?.wind?.deg ?: 0}°",
                modifier = Modifier.weight(1f)
            )

            // Rainfall
            DetailCard(
                title = "RAINFALL",
                mainText = "${weather?.rain?.oneHour ?: 1.8} mm",
                secondaryText = "in last hour",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    mainText: String,
    secondaryText: String? = null,
    footerText: String? = null,
    hasProgressBar: Boolean = false,
    progress: Float = 0f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFF2E335A).copy(alpha = 0.5f), RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mainText,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            if (secondaryText != null) {
                Text(
                    text = secondaryText,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            if (hasProgressBar) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF3E3C6E), Color(0xFFC427FB), Color(0xFFE0D9FF))
                                )
                            )
                    )
                }
            }

            if (footerText != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color.White.copy(alpha = 0.1f), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = footerText,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun formatTime(timestamp: Long, timezoneOffset: Int): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    val tzId = TimeZone.getAvailableIDs(timezoneOffset * 1000).firstOrNull() ?: "UTC"
    sdf.timeZone = TimeZone.getTimeZone(tzId)
    return sdf.format(Date(timestamp * 1000))
}
