package com.rescuemate.app.presentation.ambulance

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rescuemate.app.R
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.dto.AmbulanceRequestStatus
import com.rescuemate.app.dto.mock
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.presentation.viewmodel.AmbulanceVM
import com.rescuemate.app.presentation.viewmodel.FcmVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.repository.fcm.Message
import com.rescuemate.app.repository.fcm.Notification
import com.rescuemate.app.repository.fcm.NotificationReq
import com.rescuemate.app.utils.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun AmbulanceRequestsScreen(
    ambulanceVM: AmbulanceVM = hiltViewModel(),
    fcmVM: FcmVM = hiltViewModel(),
    navController: NavHostController,
) {
    val context = LocalContext.current
    val progressBar = remember { context.progressBar() }
    var selectedRequest by remember { mutableStateOf<AmbulanceRequest?>(null) }
    val scope = rememberCoroutineScope()
    val requests = ambulanceVM.requests

    LaunchedEffect(key1 = Unit) {
        ambulanceVM.getAmbulanceOwnerRequests(context)
    }
    progressBar.isVisible(ambulanceVM.isLoading)

    LazyColumn(modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp)) {
        item {
            Box(modifier = Modifier.height(60.dp)) {
                TopBar(text = "Ambulance Requests") {
                    navController.popBackStack()
                }
            }
        }
        items(requests) { ambulanceRequest ->
            AmbulanceRequestItem(ambulanceRequest = ambulanceRequest) {
                selectedRequest = ambulanceRequest
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    BackHandler(enabled = progressBar.isShowing) {
        progressBar.dismiss()
        return@BackHandler
    }

    if (!ambulanceVM.isLoading && requests.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "No Requests Found", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }

    AnimatedVisibility(
        visible = selectedRequest != null,
        enter = slideInHorizontally { it },
    ) {
        val request = selectedRequest ?: return@AnimatedVisibility
        Surface {
            AmbulanceRequestDetailScreen(ambulanceRequest = request, onBack = {
                selectedRequest = null
            }, onUpdatedReq = { newStatus ->
                scope.launch {
                    ambulanceVM.updatedRequest(newStatus, selectedRequest!!.id).collect {
                        when(it){
                            is Result.Failure -> {
                                context.showToast(it.exception.message ?: "Failed to update Request Try again!")
                            }
                            is Result.Success -> {

                                val notificationReq = NotificationReq(
                                    message = Message(
                                        token = selectedRequest!!.patient.token,
                                        notification = Notification(
                                            title = "Your ambulance Request is ${newStatus.name}!",
                                            body = if (newStatus == AmbulanceRequestStatus.Accepted) "Ambulance will arrive soon!" else "Please place new Request!"
                                        )
                                    )
                                )
                                ambulanceVM.getAmbulanceOwnerRequests(context)
                                fcmVM.sendPushNotification(notificationReq)
                                context.showToast("Request is updated")
                                selectedRequest = null
                            }
                            else -> {}
                        }

                    }
                }
            })
        }
    }

}

@Composable
fun AmbulanceRequestItem(ambulanceRequest: AmbulanceRequest, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickableWithOutRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier,verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = ambulanceRequest.patient.profilePicUrl,
                        contentDescription = "",
                        placeholder = painterResource(id = R.drawable.ic_profile),
                        error = painterResource(id = R.drawable.ic_profile),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)

                    )
                    Column {
                        Text(
                            text = ambulanceRequest.patient.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 10.dp),
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = ambulanceRequest.address,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2, color = Color.DarkGray, textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .widthIn(max = 180.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ambulanceRequest.status.name.uppercase(),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }

            Text(
                text = "Click to View Location & Contact",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2, fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun AmbulanceRequestItemPreview() {
    RescueMateTheme {
        Box(modifier = Modifier.background(Color.White)) {
            AmbulanceRequestItem(ambulanceRequest = AmbulanceRequest.mock) {

            }
        }
    }
}