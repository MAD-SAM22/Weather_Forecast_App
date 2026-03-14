package com.example.weatherapp.data.source.remote

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(onLocationReceived: (lat: Double, lon: Double) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location.latitude, location.longitude)
            } else {
                requestNewLocationData(onLocationReceived)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(onLocationReceived: (Double, Double) -> Unit) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation
                if (lastLocation != null) {
                    onLocationReceived(lastLocation.latitude, lastLocation.longitude)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }, Looper.getMainLooper())
    }
}