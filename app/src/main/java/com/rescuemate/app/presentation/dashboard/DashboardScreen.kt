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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rescuemate.app.R
import com.rescuemate.app.dto.User
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.ambulance.AmbulanceDashBoardScreen
import com.rescuemate.app.presentation.blooddonor.BloodDonorDashBoardScreen
import com.rescuemate.app.presentation.laboratory.LaboratoryDashBoardScreen
import com.rescuemate.app.presentation.patient.PatientDashboardScreen
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.UserStorageVM

@Composable
fun DashboardScreen(
    navHostController: NavHostController,
    userStorageVM: UserStorageVM = hiltViewModel(),
) {
    val user = userStorageVM.user ?: return
    DashboardScreenContent(
        navHostController = navHostController,
        user = user,
        actionLogout = {
            userStorageVM.removeUserData()
            navHostController.navigate(Routes.SignInScreen) {
                popUpTo(navHostController.graph.id)
            }
        },
        actionProfile = {

        }
    )
}

@Composable
fun DashboardScreenContent(user: User, actionLogout: () -> Unit, actionProfile: () -> Unit, navHostController: NavHostController) {
    var contentState by remember { mutableStateOf(true) } // Add Better Approach in Free Time
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        TopBarContent(user = user, modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 20.dp))
        MainContent(navHostController = navHostController, contentState = contentState , user = user, modifier = Modifier.align(Alignment.Center))
        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            actionLogout = actionLogout,
            actionProfile = actionProfile,
            actionHome = {
                contentState = !contentState
            }
        )
    }

}

@Composable
fun MainContent(user: User, modifier: Modifier, contentState: Boolean, navHostController: NavHostController) {
    Box(modifier = modifier) {
        when (user.userType) {
            UserType.Patient -> {
                PatientDashboardScreen(contentState = contentState, navHostController = navHostController)
            }

            UserType.AmbulanceOwner -> {
                AmbulanceDashBoardScreen()
            }

            UserType.Donor -> {
                BloodDonorDashBoardScreen(user = user, navHostController = navHostController)
            }

            UserType.LaboratoryOwner -> {
                LaboratoryDashBoardScreen()
            }
        }
    }
}

@Composable
fun TopBarContent(user: User, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            AsyncImage(
                model = user.profilePicUrl,
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.ic_profile),
                error = painterResource(id = R.drawable.ic_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickableWithOutRipple { }
            )

            Spacer(modifier = Modifier.width(2.dp))
            Column {
                Text(
                    text = user.name,
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
fun BottomNavBar(modifier: Modifier = Modifier, actionLogout: () -> Unit, actionProfile: () -> Unit, actionHome: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickableWithOutRipple { actionProfile() }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickableWithOutRipple { actionHome() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickableWithOutRipple { actionLogout() }
        )
    }
}


//@Preview
//@Composable
//fun DashboardScreenPreview() {
//    val user = User(
//        userId = "Rescue Mate",
//        name = "name",
//        email = "email",
//        profilePicUrl = "https://firebasestorage.googleapis.com/v0/b/rescuemate-c1591.appspot.com/o/Users%2FuWO5rYodBORJ2ir3OLHZaalP8kI2?alt=media&token=cf8b6a80-f0cf-450b-b7d3-591e35cdf142",
//        userType = UserType.Patient,
//    )
//    DashboardScreenContent(user = user, actionLogout = {}, actionProfile = {}, navHostController = navHostController)
//}