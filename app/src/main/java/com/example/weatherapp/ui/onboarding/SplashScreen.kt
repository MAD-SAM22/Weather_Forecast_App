package com.example.weatherapp.ui.onboarding

import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WeatherSplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }
    val floatY = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        // Initial logo animation
        scale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
        alpha.animateTo(1f, tween(1000))
        floatY.animateTo(0f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
        delay(300)
        // Content fade in
        contentAlpha.animateTo(1f, tween(800))
        delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        com.example.weatherapp.ui.theme.LightBackgroundGradientStart,
                        com.example.weatherapp.ui.theme.LightBackgroundGradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/onboarding/weather_app_logo_3d.png")
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        this.alpha = alpha.value
                        translationY = floatY.value
                    },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(contentAlpha.value)
            ) {
                Text(
                    text = "Weazy",
                    fontSize = 42.sp,
                    color = com.example.weatherapp.ui.theme.LightTextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Know the weather anywhere, anytime",
                    fontSize = 16.sp,
                    color = com.example.weatherapp.ui.theme.LightTextSecondary,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
