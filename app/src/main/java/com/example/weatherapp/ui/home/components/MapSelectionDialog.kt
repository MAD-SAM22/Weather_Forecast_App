package com.example.weatherapp.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapSelectionDialog(
    initialLocation: GeoPoint,
    onDismiss: () -> Unit,
    onLocationConfirmed: (GeoPoint) -> Unit
) {
    var selectedLocation by remember { mutableStateOf(initialLocation) }

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
                    text = "Select Location",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.weatherapp.ui.theme.LightTextPrimary
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = com.example.weatherapp.ui.theme.LightTextSecondary.copy(alpha = 0.1f)
                    )
                ) {
                    AndroidView(
                        factory = { context ->
                            Configuration.getInstance()
                                .load(context, context.getSharedPreferences("osmdroid", 0))
                            MapView(context).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(10.0)
                                controller.setCenter(initialLocation)

                                val marker = Marker(this)
                                marker.position = initialLocation
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                overlays.add(marker)

                                val eventsReceiver = object : MapEventsReceiver {
                                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                                        selectedLocation = p
                                        marker.position = p
                                        invalidate()
                                        return true
                                    }

                                    override fun longPressHelper(p: GeoPoint): Boolean {
                                        return false
                                    }
                                }
                                overlays.add(MapEventsOverlay(eventsReceiver))
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Button(
                    onClick = { onLocationConfirmed(selectedLocation) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.weatherapp.ui.theme.PrimaryPurple)
                ) {
                    Text("Confirm Location", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
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