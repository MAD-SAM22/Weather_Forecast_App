package com.example.weatherapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.data.model.ForecastItem

@Composable
fun ForecastCard(item: ForecastItem) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(146.dp)
            .background(
                if (item.isSelected) Color(0xFF48319D) else Color(0xFF2E335A).copy(alpha = 0.4f),
                RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
        ) {
            Text(text = item.time, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/${item.iconAssetPath}")
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(text = item.temp, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }
    }
}
