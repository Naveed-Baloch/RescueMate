package com.rescuemate.app.presentation.ambulance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.dto.AmbulanceRequestStatus
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.openDialPanel
import com.rescuemate.app.extensions.openWhatsApp
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.utils.TopBar

@Composable
fun AmbulanceRequestDetailScreen(ambulanceRequest: AmbulanceRequest, onBack: () -> Unit, onUpdatedReq: (AmbulanceRequestStatus) -> Unit) {

    val latLng = remember { LatLng(ambulanceRequest.lat, ambulanceRequest.lag) }
    val markerState by remember { mutableStateOf(MarkerState(position = latLng)) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(latLng, 10f) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(enabled = false) { },
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .padding(start = 10.dp, top = 30.dp)
        ) {
            TopBar(text = "Request Details", onBack = onBack)
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.75f),
        ) {
            Marker(state = markerState)
        }

        Column(
            modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = "Name: ${ambulanceRequest.patient.name}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Address: ${ambulanceRequest.address}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(primaryColor)
                        .clickableWithOutRipple {
                            context.openWhatsApp(ambulanceRequest.patient.phoneNumber)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chat".uppercase(),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(primaryColor)
                        .clickableWithOutRipple {
                            context.openDialPanel(ambulanceRequest.patient.phoneNumber)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Call".uppercase(),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
            ) {
                if (ambulanceRequest.status == AmbulanceRequestStatus.Pending) {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(primaryColor)
                            .clickableWithOutRipple {
                                onUpdatedReq(AmbulanceRequestStatus.Rejected)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Reject".uppercase(),
                            color = Color.White, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(primaryColor)
                        .clickableWithOutRipple {
                            if (ambulanceRequest.status == AmbulanceRequestStatus.Pending) {
                                onUpdatedReq(AmbulanceRequestStatus.Accepted)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (ambulanceRequest.status == AmbulanceRequestStatus.Pending) "Accept" else ambulanceRequest.status.name.uppercase(),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}
