package com.rescuemate.app.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.rescuemate.app.R
import com.rescuemate.app.dto.User
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.dto.mock
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.ambulance.AmbulanceDashBoardScreen
import com.rescuemate.app.presentation.blooddonor.BloodDonorDashBoardScreen
import com.rescuemate.app.presentation.laboratory.LaboratoryDashBoardScreen
import com.rescuemate.app.presentation.maps.Permissions
import com.rescuemate.app.presentation.maps.getAddressFromLatLng
import com.rescuemate.app.presentation.patient.PatientDashboardScreen
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.FcmVM
import com.rescuemate.app.presentation.viewmodel.LocationViewModel
import com.rescuemate.app.presentation.viewmodel.UserStorageVM

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DashboardScreen(
    navHostController: NavHostController,
    locationViewModel: LocationViewModel = hiltViewModel(),
    fcmVM: FcmVM = hiltViewModel(),
    user: User,
    userStorageVM: UserStorageVM = hiltViewModel(),
) {
    val context = LocalContext.current
    val currentLocation = locationViewModel.currentLocation
    var currentUserAddress by remember { mutableStateOf("") }
    LaunchedEffect(key1 = currentLocation) {
        if (currentLocation != null) {
            val lat = currentLocation.latitude
            val lng = currentLocation.longitude
            getAddressFromLatLng(lat = lat, lng = lng, context = context, onAddressReceive = {
                currentUserAddress = it
            })
        }
    }

    LaunchedEffect(key1 = user) {
        fcmVM.refreshToken()
    }

    Permissions()

    LaunchedEffect(key1 = Unit) {
        locationViewModel.getCurrentLocation()
    }

    DashboardScreenContent(
        navHostController = navHostController,
        currentUserAddress = currentUserAddress,
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
fun DashboardScreenContent(user: User, actionLogout: () -> Unit, actionProfile: () -> Unit, navHostController: NavHostController, currentUserAddress: String) {
    var contentState by remember { mutableStateOf(true) } // Add Better Approach in Free Time
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        TopBarContent(
            user = user, currentUserAddress = currentUserAddress, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
        )
        MainContent(navHostController = navHostController, contentState = contentState, user = user, modifier = Modifier.align(Alignment.Center))
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
                AmbulanceDashBoardScreen(user = user, navHostController = navHostController)
            }

            UserType.Donor -> {
                BloodDonorDashBoardScreen(user = user, navHostController = navHostController)
            }

            UserType.LaboratoryOwner -> {
                LaboratoryDashBoardScreen(user = user, navHostController = navHostController)
            }
        }
    }
}

@Composable
fun TopBarContent(user: User, modifier: Modifier = Modifier, currentUserAddress: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = user.profilePicUrl,
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.ic_profile),
                error = painterResource(id = R.drawable.ic_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable(enabled = false) {}
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
                    text = "Name", //
                    style = MaterialTheme.typography.bodySmall,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.clickableWithOutRipple {
                        // Navigate to User Profile
                    }
                )
            }
        }
        if (currentUserAddress.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = currentUserAddress,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Start,
                        modifier = Modifier.widthIn(max = 130.dp), maxLines = 2,
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
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier, actionLogout: () -> Unit, actionProfile: () -> Unit, actionHome: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

//        Image(
//            painter = painterResource(id = R.drawable.ic_profile),
//            contentDescription = "",
//            modifier = Modifier
//                .size(40.dp)
//                .clickableWithOutRipple { actionProfile() }
//        )

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


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreenContent(
        user = User.mock.copy(userType = UserType.Patient),
        currentUserAddress = "Ali Town Lahore!",
        navHostController = rememberNavController(),
        actionProfile = {},
        actionLogout = {}
    )
}