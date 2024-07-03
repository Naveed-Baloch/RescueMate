package com.rescuemate.app.presentation.ambulance

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rescuemate.app.R
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.presentation.maps.CheckLocationPermissions
import com.rescuemate.app.presentation.maps.getAddressFromLatLng
import com.rescuemate.app.presentation.viewmodel.AmbulanceVM
import com.rescuemate.app.presentation.viewmodel.LocationViewModel
import com.rescuemate.app.utils.CityDropDown
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.TopBar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AmbulanceRequestScreen(navHostController: NavHostController, ambulanceVM: AmbulanceVM = hiltViewModel(), locationViewModel: LocationViewModel = hiltViewModel()) {
    var city by remember { mutableStateOf("") }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lng by remember { mutableDoubleStateOf(0.0) }
    var address by remember { mutableStateOf("") }
    var checkLocationPermissions by remember { mutableStateOf(false) }
    val markerState by remember(lng, lat) { mutableStateOf(MarkerState(position = LatLng(lat, lng))) }
    val cameraPosition = remember { LatLng(31.5204, 74.3587) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPosition, 10f)
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = lat, lng) {
        if (lat != 0.0 && lng != 0.0) {
             getAddressFromLatLng(lat = lat, lng = lng, context = context, onAddressReceive = {
                 address = it
             })
        }
    }

    val currentLocation = locationViewModel.currentLocation
    LaunchedEffect(key1 = currentLocation) {
        if(currentLocation != null) {
            lat = currentLocation.latitude
            lng = currentLocation.longitude
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(LatLng(currentLocation.latitude, cameraPosition.longitude), 15f, 0f, 0f)
                ),
                durationMs = 1000
            )
        }
    }

    if(checkLocationPermissions) {
        CheckLocationPermissions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .padding(start = 10.dp, top = 30.dp)
        ) {
            TopBar(text = "Request Ambulance") { navHostController.popBackStack() }
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            onMapClick = {
                lat = it.latitude
                lng = it.longitude
            },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.55f),
        ) {
            if (lat != 0.0 && lng != 0.0) {
                Marker(state = markerState)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Select city",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            CityDropDown(
                modifier = Modifier.fillMaxWidth(),
                onCitySelected = { city = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Current Address",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomEditText(
                value = address,
                label = "Enter your Current Address",
                isError = false,
                onValueChange = {
                    address = it
                },
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_current_location),
                        contentDescription = "",
                        modifier = Modifier.clickableWithOutRipple {
                            locationViewModel.currentLocation = null
                            locationViewModel.getCurrentLocation()
                            checkLocationPermissions = true
                        })
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Red)
                    .clickableWithOutRipple {
                        if (city.isNotEmpty() && lat != 0.0 && lng != 0.0) {

                        } else if (city.isEmpty()) {
                            context.showToast("Something is missing!")
                        } else {
                            context.showToast("Please Select Location from Map!")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Request Ambulance".uppercase(),
                    color = Color.White, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}
