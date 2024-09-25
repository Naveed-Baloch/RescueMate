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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.messaging.Message
import com.rescuemate.app.messaging.NotificationReq
import com.rescuemate.app.messaging.Priority
import com.rescuemate.app.presentation.maps.Permissions
import com.rescuemate.app.presentation.maps.getAddressFromLatLng
import com.rescuemate.app.presentation.viewmodel.AmbulanceVM
import com.rescuemate.app.presentation.viewmodel.FcmVM
import com.rescuemate.app.presentation.viewmodel.LocationViewModel
import com.rescuemate.app.utils.CityDropDown
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.TopBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AmbulanceRequestScreen(
    navHostController: NavHostController,
    ambulanceVM: AmbulanceVM = hiltViewModel(),
    fcmVM: FcmVM = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
) {
    var city by remember { mutableStateOf("") }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lng by remember { mutableDoubleStateOf(0.0) }
    var address by remember { mutableStateOf("") }
    var checkLocationPermissions by remember { mutableStateOf(false) }
    var showRequestSubmittedDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
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
    val progressBar = remember { context.progressBar() }
    progressBar.isVisible(ambulanceVM.isLoading)

    if(checkLocationPermissions) {
        Permissions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).padding(top = 5.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .height(45.dp)
                .padding(start = 10.dp)
        ) {
            TopBar(text = "Request Ambulance") { navHostController.navigateUp() }
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            onMapClick = {
                lat = it.latitude
                lng = it.longitude
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
        ) {
            if (lat != 0.0 && lng != 0.0) {
                Marker(state = markerState)
            }
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(10.dp))
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

            Spacer(modifier = Modifier.height(10.dp))

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

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Red)
                    .clickableWithOutRipple {
                        if (city.isNotEmpty() && lat != 0.0 && lng != 0.0 && address.isNotEmpty()) {
                            ambulanceVM.processAddRequest(
                                city = city,
                                lat = lat,
                                lng = lng,
                                address = address,
                                context = context
                            ) { ambulanceOwnerToken, requestId ->
                                val notificationReq = NotificationReq(
                                    message = Message(
                                        token = ambulanceOwnerToken,
                                        data = mapOf(
                                            "requestId" to requestId,
                                            "title" to "You have new ambulance request from ${ambulanceVM.user?.name}",
                                            "body" to "At: $address"
                                        ),
                                        priority = Priority("high")
                                    )
                                )
                                scope.launch {
                                    fcmVM.sendPushNotification(notificationReq)
                                    showRequestSubmittedDialog = true
                                }
                            }

                        } else if (city.isEmpty() || address.isNotEmpty()) {
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

    if (showRequestSubmittedDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.1f))
        ) {
            RequestAddedDialog(onDismissRequest = {
                showRequestSubmittedDialog = false
                navHostController.navigateUp()
            })
        }
    }

}

@Composable
fun RequestAddedDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Your Request is Submitted!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Ambulance driver will response to your request",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Red)
                    .clickableWithOutRipple { onDismissRequest() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ok".uppercase(),
                    color = Color.White, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}
