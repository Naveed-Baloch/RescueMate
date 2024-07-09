package com.rescuemate.app.presentation.ambulance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.dto.User
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.presentation.viewmodel.AmbulanceVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.CityDropDown
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.TopBar
import kotlinx.coroutines.launch


@Composable
fun AmbulanceScreen(
    user: User,
    navHostController: NavHostController,
    ambulanceVM: AmbulanceVM = hiltViewModel(),
) {
    val context = LocalContext.current
    val progressBar = remember { context.progressBar() }
    val scope = rememberCoroutineScope()

    progressBar.isVisible(ambulanceVM.isLoading)

    AmbulanceContent(
        actionAddAmbulance = { vehicleNumber, licenceNumber, city, isAvailable ->
            val ambulance = Ambulance(
                ownerId = user.userId,
                vehicleNumber = vehicleNumber,
                licenceNumber = licenceNumber,
                city = city,
                isAvailable = isAvailable
            )
            scope.launch {
                ambulanceVM.addAmbulance(ambulance).collect {
                    when (it) {
                        is Result.Failure -> {
                            context.showToast(it.exception.message ?: "Something went Wrong")
                        }

                        is Result.Success -> {
                            context.showToast(it.data)
                            progressBar.dismiss()
                            navHostController.navigateUp()
                        }
                        else -> {}
                    }
                }
            }

        },
        onBack = {
            navHostController.navigateUp()
        }
    )
}


@Composable
fun AmbulanceContent(
    actionAddAmbulance: (
        vehicleNumber: String,
        licenceNumber: String,
        city: String,
        isisAvailable: Boolean,
    ) -> Unit,
    onBack: () -> Unit,
) {
    var vehicleNumber by remember { mutableStateOf("") }
    var licenceNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isAvailable by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 30.dp, horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(text = "Add Ambulance") {
            onBack()
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Vehicle Number",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = vehicleNumber,
            label = "Enter Vehicle Number",
            isError = false,
            onValueChange = {
                vehicleNumber = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Licence Number",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = licenceNumber,
            label = "Enter Licence Number",
            isError = false,
            onValueChange = {
                licenceNumber = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Set Ambulance Availability",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.1f))
                .padding(vertical = 15.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Is Available",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier
            )
            Switch(
                checked = isAvailable,
                onCheckedChange = {
                    isAvailable = it
                }
            )

        }

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red)
                .clickableWithOutRipple {
                    if (vehicleNumber.isNotEmpty() && licenceNumber.isNotEmpty() && city.isNotEmpty()) {
                        actionAddAmbulance(
                            vehicleNumber,
                            licenceNumber,
                            city,
                            isAvailable
                        )
                    } else {
                        context.showToast("Something is missing!")

                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Add".uppercase(),
                color = Color.White, fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }
    }
}

@Preview
@Composable
fun LaboratoryContentPreview() {
    AmbulanceContent(actionAddAmbulance = { _, _, _, _ -> }) {}
}