package com.example.weatherapp.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.ui.theme.PrimaryPurple
import com.example.weatherapp.ui.theme.ForecastCardTop
import com.example.weatherapp.ui.theme.ForecastCardBottom
import com.example.weatherapp.ui.theme.OnboardingCardBg
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.collections.listOf
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
// ─── Data ───────────────────────────────────────────────────────────────────

data class OnboardingData(
    val title: String,
    val description: String,
    val tag: String,
    val assetPath: String,
    val accentColor: Color = PrimaryPurple
)

private val pages = listOf(
    OnboardingData(
        title = "Know the Weather\nAnywhere, Anytime",
        description = "Real-time weather for any city on Earth. Rain, sun or snow — stay one step ahead wherever you go.",
        tag = "Weather",
        assetPath = "onboarding/multi_weather.png"
    ),
    OnboardingData(
        title = "Set Alerts &\nMorning Notifications",
        description = "Custom weather alerts and a daily morning forecast delivered right to you. Never be caught off guard again.",
        tag = "Notifications",
        assetPath = "onboarding/allert.png"
    ),
    OnboardingData(
        title = "Add Cities to\nYour Favourites",
        description = "Save the cities you love. Switch between them instantly and plan ahead with beautiful per-city weather cards.",
        tag = "Favourites",
        assetPath = "onboarding/location_onboarding.png"
    )
)

// ─── Main Screen ─────────────────────────────────────────────────────────────

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Background consistent with Home theme ───────────────────
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
        )

        // ── Pages ───────────────────────────────────────────────────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            // Calculate the page page offset for animation
            val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            OnboardingPageContent(
                data = pages[index],
                pageIndex = index,
                pageOffset = pageOffset
            )
        }

        // ── Bottom controls ─────────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 52.dp, start = 32.dp, end = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dot indicators matching premium style
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.indices.forEach { i ->
                    val selected = i == currentPage
                    val width by animateDpAsState(
                        targetValue = if (selected) 24.dp else 8.dp,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy),
                        label = "dot"
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(
                                if (selected) com.example.weatherapp.ui.theme.PrimaryPurple
                                else com.example.weatherapp.ui.theme.PrimaryPurple.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary action button — Circular "Add" style from Home
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(Color.White, Color(0xFFE0E0E0))),
                        shape = CircleShape
                    )
                    .clickable {
                        if (currentPage < pages.size - 1) {
                            scope.launch { pagerState.animateScrollToPage(currentPage + 1) }
                        } else {
                            onFinished()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(com.example.weatherapp.ui.theme.PrimaryPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentPage < pages.size - 1) "→" else "✓",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skip
            if (currentPage < pages.size - 1) {
                Text(
                    text = "Skip",
                    color = com.example.weatherapp.ui.theme.LightTextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onFinished() }
                )
            }
        }
    }
}

// ─── Single page content ───────────────────────────────────────────────────

@Composable
fun OnboardingPageContent(
    data: OnboardingData,
    pageIndex: Int,
    pageOffset: Float
) {
    val context = LocalContext.current
    
    // Animation calculations based on scroll offset
    val imageScale = 1f - (kotlin.math.abs(pageOffset) * 0.15f).coerceIn(0f, 0.4f)
    val imageTranslation = pageOffset * 200f
    
    val cardAlpha = 1f - (kotlin.math.abs(pageOffset) * 0.8f).coerceIn(0f, 1f)
    val cardTranslation = pageOffset * 100f
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.12f))

        // ── High Quality Illustration ──────────────────────────────
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${data.assetPath}")
                .build(),
            contentDescription = null,
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                    translationX = imageTranslation
                },
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ── Glass Text Card - Matching Lighter Theme ────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = cardAlpha
                    translationX = cardTranslation
                }
                .clip(RoundedCornerShape(44.dp))
                .border(2.dp, com.example.weatherapp.ui.theme.LightGlassCardBorder, RoundedCornerShape(44.dp))
                .background(com.example.weatherapp.ui.theme.LightGlassCardBg)
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Accent tag pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(com.example.weatherapp.ui.theme.PrimaryPurple.copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = data.tag.uppercase(),
                        color = com.example.weatherapp.ui.theme.PrimaryPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = data.title,
                    color = com.example.weatherapp.ui.theme.LightTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = data.description,
                    color = com.example.weatherapp.ui.theme.LightTextSecondary,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
