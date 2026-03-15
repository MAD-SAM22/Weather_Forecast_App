package com.example.weatherapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val currentLanguage by viewModel.language.collectAsState()
    val currentUnits by viewModel.units.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2E335A), Color(0xFF1C1B33))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Language Setting
                SettingsSection(title = "Language") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SettingChip(
                            text = "English",
                            selected = currentLanguage == "en",
                            onClick = { viewModel.setLanguage("en") }
                        )
                        SettingChip(
                            text = "العربية",
                            selected = currentLanguage == "ar",
                            onClick = { viewModel.setLanguage("ar") }
                        )
                    }
                }

                // Units Setting
                SettingsSection(title = "Units") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SettingChip(
                                text = "Metric (°C, m/s)",
                                selected = currentUnits == "metric",
                                onClick = { viewModel.setUnits("metric") }
                            )
                            SettingChip(
                                text = "Imperial (°F, mph)",
                                selected = currentUnits == "imperial",
                                onClick = { viewModel.setUnits("imperial") }
                            )
                        }
                        SettingChip(
                            text = "Standard (K)",
                            selected = currentUnits == "standard",
                            onClick = { viewModel.setUnits("standard") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}

@Composable
fun SettingChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) Color(0xFF48319D) else Color.White.copy(alpha = 0.1f))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
