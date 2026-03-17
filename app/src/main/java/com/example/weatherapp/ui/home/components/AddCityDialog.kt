package com.example.weatherapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.model.GeocodingResponseItem

@Composable
fun AddCityDialog(
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<GeocodingResponseItem>,
    onCitySelected: (GeocodingResponseItem) -> Unit,
    onSelectOnMap: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

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
                    text = "Add New City",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.weatherapp.ui.theme.LightTextPrimary
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearch(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Search City...",
                            color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(
                                alpha = 0.6f
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = com.example.weatherapp.ui.theme.PrimaryPurple
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = com.example.weatherapp.ui.theme.PrimaryPurple,
                        unfocusedBorderColor = com.example.weatherapp.ui.theme.LightTextSecondary.copy(
                            alpha = 0.2f
                        ),
                        cursorColor = com.example.weatherapp.ui.theme.PrimaryPurple,
                        focusedLabelColor = com.example.weatherapp.ui.theme.PrimaryPurple,
                        unfocusedLabelColor = com.example.weatherapp.ui.theme.LightTextSecondary
                    )
                )

                if (searchResults.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .background(
                                color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(
                                    alpha = 0.05f
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(4.dp)
                    ) {
                        LazyColumn {
                            items(searchResults) { city ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onCitySelected(city) }
                                        .padding(vertical = 12.dp, horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = "${city.name}, ${city.country}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = com.example.weatherapp.ui.theme.LightTextPrimary
                                    )
                                    if (city.state != null) {
                                        Text(
                                            text = city.state,
                                            fontSize = 12.sp,
                                            color = com.example.weatherapp.ui.theme.LightTextSecondary
                                        )
                                    }
                                }
                                if (searchResults.last() != city) {
                                    HorizontalDivider(
                                        thickness = 0.5.dp,
                                        color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(
                                            alpha = 0.1f
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onSelectOnMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.weatherapp.ui.theme.PrimaryPurple)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Select on Map", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Cancel",
                        color = com.example.weatherapp.ui.theme.LightTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}