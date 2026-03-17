package com.example.weatherapp.ui.alerts.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.GeocodingResponseItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddAlertDialog(
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<GeocodingResponseItem>,
    onConfirm: (String, String, Date, String) -> Unit
) {
    val context = LocalContext.current
    var cityName by remember { mutableStateOf("") }
    var alertType by remember { mutableStateOf("Notification") }
    val calendar = remember { Calendar.getInstance() }
    
    var selectedDate by remember { mutableStateOf(calendar.time) }
    var selectedTime by remember { mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = com.example.weatherapp.ui.theme.LightBackgroundGradientEnd
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_weather_alert),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.weatherapp.ui.theme.LightTextPrimary
                )

                OutlinedTextField(
                    value = cityName,
                    onValueChange = {
                        cityName = it
                        onSearch(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.city_name)) },
                    placeholder = { Text("Search City...", color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = com.example.weatherapp.ui.theme.PrimaryPurple) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = com.example.weatherapp.ui.theme.PrimaryPurple,
                        unfocusedBorderColor = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.2f),
                        cursorColor = com.example.weatherapp.ui.theme.PrimaryPurple,
                        focusedLabelColor = com.example.weatherapp.ui.theme.PrimaryPurple,
                        unfocusedLabelColor = com.example.weatherapp.ui.theme.LightTextSecondary
                    )
                )

                if (searchResults.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .background(
                                color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.05f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(4.dp)
                    ) {
                        LazyColumn {
                            items(searchResults) { city ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { 
                                            cityName = city.name
                                            onSearch("") // clear results
                                        }
                                        .padding(vertical = 12.dp, horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = "${city.name}, ${city.country}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = com.example.weatherapp.ui.theme.LightTextPrimary
                                    )
                                }
                                if (searchResults.last() != city) {
                                    HorizontalDivider(
                                        thickness = 0.5.dp,
                                        color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.1f)
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = alertType == "Notification",
                        onClick = { alertType = "Notification" },
                        colors = RadioButtonDefaults.colors(selectedColor = com.example.weatherapp.ui.theme.PrimaryPurple)
                    )
                    Text(stringResource(R.string.notification), color = com.example.weatherapp.ui.theme.LightTextPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = alertType == "Alarm",
                        onClick = { alertType = "Alarm" },
                        colors = RadioButtonDefaults.colors(selectedColor = com.example.weatherapp.ui.theme.PrimaryPurple)
                    )
                    Text(stringResource(R.string.alarm), color = com.example.weatherapp.ui.theme.LightTextPrimary)
                }

                Button(
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                selectedDate = calendar.time
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = com.example.weatherapp.ui.theme.PrimaryPurple)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(selectedDate),
                        color = com.example.weatherapp.ui.theme.LightTextPrimary
                    )
                }

                Button(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = com.example.weatherapp.ui.theme.PrimaryPurple)
                    Spacer(Modifier.width(8.dp))
                    Text(selectedTime, color = com.example.weatherapp.ui.theme.LightTextPrimary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { 
                        if (cityName.isNotBlank()) onConfirm(cityName, alertType, selectedDate, selectedTime) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.weatherapp.ui.theme.PrimaryPurple)
                ) {
                    Text(stringResource(R.string.add), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        color = com.example.weatherapp.ui.theme.LightTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
