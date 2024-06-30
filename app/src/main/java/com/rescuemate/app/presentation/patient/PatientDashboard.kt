package com.rescuemate.app.presentation.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rescuemate.app.R
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.utils.ActionButton

enum class PatientDashboardUiState{
    DashBoard, Emergency , NonEmergency
}

@Composable
fun PatientDashboardScreen() {
    val uiState by remember { mutableStateOf(PatientDashboardUiState.NonEmergency) }
    when (uiState) {
        PatientDashboardUiState.DashBoard -> {
            PatientDashboardScreenContent()
        }

        PatientDashboardUiState.Emergency -> {
            PatientEmergencyServices()
        }

        PatientDashboardUiState.NonEmergency -> {
            PatientNonEmergencyServices()
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientDashboardScreenContent(){
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
                onClick = {

                }
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Non Emergency\nServices",
                onClick = {

                }
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {

                }
            )

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientEmergencyServices(){
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
                onClick = {

                }
            )
            ActionButton(
                imageId = R.drawable.ic_blood_donor,
                description = "Find Blood Donor",
                onClick = {

                }
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {

                }
            )

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientNonEmergencyServices(){
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
                imageId = R.drawable.ic_ambulance,
                description = "Book Ambulance\n",
                onClick = {

                }
            )
            ActionButton(
                imageId = R.drawable.ic_blood_donor,
                description = "Find Medical Test Centers",
                onClick = {

                }
            )
            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {

                }
            )
        }
    }
}


@Preview
@Composable
fun PatientDashboardScreenPreview(){
    RescueMateTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ) {
            PatientDashboardScreen()
        }
    }
}
