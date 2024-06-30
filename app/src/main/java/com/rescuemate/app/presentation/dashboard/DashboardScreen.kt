package com.rescuemate.app.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rescuemate.app.R
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.presentation.ambulance.AmbulanceDashBoardScreen
import com.rescuemate.app.presentation.blooddonor.BloodDonorDashBoardScreen
import com.rescuemate.app.presentation.laboratory.LaboratoryDashBoardScreen
import com.rescuemate.app.presentation.patient.PatientDashboardScreen
import com.rescuemate.app.presentation.theme.primaryColor

@Composable
fun DashboardScreen() {
    DashboardScreenContent(UserType.LaboratoryOwner)
}

@Composable
fun DashboardScreenContent(userType: UserType) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBarContent(userType = userType, modifier = Modifier.align(Alignment.TopCenter))
        MainContent(userType = userType, modifier = Modifier.align(Alignment.Center))
        BottomNavBar(userType = userType, modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun MainContent(userType: UserType, modifier: Modifier) {
    Box(modifier = modifier) {
        when (userType) {
            UserType.Patient -> {
                PatientDashboardScreen()
            }

            UserType.AmbulanceOwner -> {
                AmbulanceDashBoardScreen()
            }

            UserType.Donor -> {
                BloodDonorDashBoardScreen()
            }

            UserType.LaboratoryOwner -> {
                LaboratoryDashBoardScreen()
            }
        }
    }
}

@Composable
fun TopBarContent(userType: UserType, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clickableWithOutRipple {

                    }
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(
                    text = "Hassan Ashfaq", // TODO Add current User Name
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 200.dp),
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "View/Edit Profile", //
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.clickableWithOutRipple {
                        // Navigate to User Profile
                    }
                )
            }
        }
        Row {
            Column {
                Text(
                    text = "Ali Town Lahore", // Todo Add Current Location
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 200.dp), maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Current location",
                    style = MaterialTheme.typography.bodySmall, color = primaryColor,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_map_pin),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clickableWithOutRipple {}
            )
        }
    }
}

@Composable
fun BottomNavBar(userType: UserType, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickableWithOutRipple {

                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickableWithOutRipple { }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickableWithOutRipple { }
        )
    }
}


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}