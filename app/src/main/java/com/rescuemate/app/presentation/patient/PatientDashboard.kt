package com.rescuemate.app.presentation.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rescuemate.app.R
import com.rescuemate.app.extensions.openEmail
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.utils.ActionButton

enum class PatientDashboardUiState {
    DashBoard, Emergency, NonEmergency
}

@Composable
fun PatientDashboardScreen(contentState: Boolean, navHostController: NavHostController) {
    var uiState by remember(contentState) { mutableStateOf(PatientDashboardUiState.DashBoard) }
    when (uiState) {
        PatientDashboardUiState.DashBoard -> {
            PatientDashboardScreenContent(
                onEmergencyServiceClick = { uiState = PatientDashboardUiState.Emergency },
                onNonEmergencyServiceClick = { uiState = PatientDashboardUiState.NonEmergency }
            )
        }

        PatientDashboardUiState.Emergency -> {
            PatientEmergencyServices(
                actionRequestAmbulance = {
                    navHostController.navigate(Routes.AmbulanceRequestScreen)
                },

                actionFindBloodDonor = {
                    navHostController.navigate(Routes.BloodRequestScreen)
                },
                actionAmbulanceRequests = {
                    navHostController.navigate(Routes.AmbulanceRequestsScreen)
                }
            )
        }

        PatientDashboardUiState.NonEmergency -> {
            PatientNonEmergencyServices(
                actionFindTestCenter = {
                    navHostController.navigate(Routes.LaboratoryRequestScreen)
                }
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientDashboardScreenContent(
    onEmergencyServiceClick: () -> Unit,
    onNonEmergencyServiceClick: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Service you Want?".uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = primaryColor,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 60.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Emergency\nServices",
                onClick = onEmergencyServiceClick
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Non Emergency\nServices",
                onClick = onNonEmergencyServiceClick
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {
                    context.openEmail("ucphassan@gmail.com")
                }
            )

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientEmergencyServices(actionRequestAmbulance: () -> Unit, actionFindBloodDonor: () -> Unit, actionAmbulanceRequests: ()-> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Emergency Services".uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = primaryColor,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 60.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            ActionButton(
                imageId = R.drawable.ic_ambulance,
                description = "Request Ambulance",
                onClick = actionRequestAmbulance
            )

            ActionButton(
                imageId = R.drawable.ic_ambulance,
                description = "Your Requests",
                onClick = actionAmbulanceRequests
            )

            ActionButton(
                imageId = R.drawable.ic_blood_donor,
                description = "Find Blood Donor",
                onClick = actionFindBloodDonor
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {
                    context.openEmail("ucphassan@gmail.com")
                }
            )

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientNonEmergencyServices(actionFindTestCenter: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Non Emergency Services".uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = primaryColor,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 60.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            ActionButton(
                imageId = R.drawable.ic_blood_donor,
                description = "Find Medical Test Centers",
                onClick = actionFindTestCenter
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {
                    context.openEmail("ucphassan@gmail.com")
                }
            )
        }
    }
}