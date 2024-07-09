package com.rescuemate.app.presentation.ambulance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.R
import com.rescuemate.app.dto.User
import com.rescuemate.app.dto.getEncodedUser
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.openEmail
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.AmbulanceVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.ActionButton


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmbulanceDashBoardScreen(
    user: User,
    ambulanceVM: AmbulanceVM = hiltViewModel(),
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val progressBar = remember { context.progressBar() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        isLoading = true
        progressBar.show()
        ambulanceVM.getUserAmbulance().collect {
            if (it is Result.Failure) {
                context.showToast(it.exception.message ?: "Something went Wrong")
            }
            if (it !is Result.Loading) {
                isLoading = false
                progressBar.isVisible(false)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome,\n${user.name}".uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = primaryColor,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 60.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth().alpha(if(isLoading) 0f else 1f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            if(ambulanceVM.userAmbulance == null ) {
                ActionButton(
                    imageId = R.drawable.ic_emergency,
                    description = if (ambulanceVM.userAmbulance == null) "Add Ambulance" else "Edit Ambulance",
                    imageModifier = Modifier.run { size(50.dp).offset(y = (-10).dp) },
                    onClick = {
                        navHostController.navigate(Routes.AmbulanceScreen(user = user.getEncodedUser()))
                    }
                )
            }
            ActionButton(
                imageId = R.drawable.ic_calendar,
                description = "Requests",
                imageModifier = Modifier.run { size(50.dp).offset(y = (-10).dp) },
                onClick = {
                    if (ambulanceVM.userAmbulance != null) {
                        navHostController.navigate(Routes.AmbulanceRequestsScreen)
                    } else {
                        context.showToast("Please Add Ambulance first!")
                    }
                }
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
