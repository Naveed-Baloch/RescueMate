package com.rescuemate.app.presentation.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissions() {
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ).apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toList()
    )

    LaunchedEffect(true){
        locationPermissions.launchMultiplePermissionRequest()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getAddressFromLatLng(
    lat: Double,
    lng: Double,
    context: Context,
    fetchCityOnly: Boolean = false,
    onAddressReceive: (String) -> Unit,
) {
    val geocoder = Geocoder(context)
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(lat, lng,1) {
            var address1: String? = null
            address1 = if(fetchCityOnly) {
                it.firstOrNull()?.locality
            } else {
                it.firstOrNull()?.getAddressLine(0)
            }
            if (address1 != null) {
                onAddressReceive.invoke(address1)
            }
        }
    } else {
        val addresses = geocoder.getFromLocation(lat, lng,1)
        var address: String? = ""
        address = if(fetchCityOnly) {
            addresses?.firstOrNull()?.locality
        } else {
            addresses?.firstOrNull()?.getAddressLine(0)
        }
        if (address != null) {
            onAddressReceive(address)
        }
    }

}