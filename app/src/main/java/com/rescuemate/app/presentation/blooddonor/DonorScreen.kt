package com.rescuemate.app.presentation.blooddonor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.dto.BloodDonor
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.presentation.viewmodel.BloodVM
import com.rescuemate.app.presentation.viewmodel.UserStorageVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.CityDropDown
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun BloodDonorScreen(
    navHostController: NavHostController,
    bloodVM: BloodVM = hiltViewModel(),
    userStorageVM: UserStorageVM = hiltViewModel(),
) {
    val user = userStorageVM.user ?: return
    val context = LocalContext.current
    var keepShowingLoading by remember { mutableStateOf(false) }
    val progressBar = remember { context.progressBar() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = bloodVM.isLoading) {
        if (!bloodVM.isLoading) delay(1000)
        keepShowingLoading = bloodVM.isLoading
        progressBar.isVisible(keepShowingLoading)
    }

    DonorScreenContent(
        actionBecomeDonor = { dob, bloodGroup, donationMonth, address, medicalHistory, isAvailable, city ->
            val bloodDonor = BloodDonor(
                userId = user.userId,
                name = user.name,
                dob = dob,
                phoneNumber = user.phoneNumber,
                profilePicUrl = user.profilePicUrl,
                bloodGroup = bloodGroup,
                address = address,
                howOftenCanDonate = donationMonth,
                medicalHistory = medicalHistory,
                isAvailable = isAvailable,
                city = city
            )
            scope.launch {
                bloodVM.addBloodDonor(bloodDonor).collect {
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
        },
    )
}


@Composable
fun DonorScreenContent(
    actionBecomeDonor: (
        dob: String,
        bloodGroup: String,
        donationMonth: Int,
        address: String,
        medicalHistory: String,
        isisAvailable: Boolean,
        city: String
    ) -> Unit,
    onBack: () -> Unit,
) {


    var dob by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isAvailable by remember { mutableStateOf(false) }
    var medicalHistory by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var donationMonth by remember { mutableIntStateOf(3) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 30.dp, horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(text = "Become Donor") {
            onBack()
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "DOB",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = dob,
            label = "Enter your Date of birth eg 12-02-2000",
            isError = dob.isEmpty(),
            onValueChange = {
                dob = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Select your Blood Group",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        BloodGroupDropdown(
            modifier = Modifier.fillMaxWidth(),
            onBloodGroupSelected = { bloodGroup = it }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "How often you want to donate?",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        BloodDonationRatioMonth(selectedRatio = donationMonth) {
            donationMonth = it
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Set your availability",
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
                text = "Availability",
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
            text = "Select your city",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CityDropDown(
            modifier = Modifier.fillMaxWidth(),
            onCitySelected = { city = it }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Current Address",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = address,
            label = "Enter your Current Address",
            isError = address.isEmpty(),
            onValueChange = {
                address = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Medical History",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = medicalHistory,
            singleLine = false,
            label = "Please Enter your medical history",
            isError = medicalHistory.isEmpty(),
            onValueChange = {
                medicalHistory = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red)
                .clickableWithOutRipple {
                    if (dob.isNotEmpty() && bloodGroup.isNotEmpty() && address.isNotEmpty() && medicalHistory.isNotEmpty()) {
                        actionBecomeDonor(
                            dob,
                            bloodGroup,
                            donationMonth,
                            address,
                            medicalHistory,
                            isAvailable,
                            city
                        )
                    } else {
                        context.showToast("Something is missing!")

                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Become Donor".uppercase(),
                color = Color.White, fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }
    }
}

@Composable
fun BloodDonationRatioMonth(selectedRatio: Int, onSelect: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        BloodDonationRatioItem(value = 3, isSelected = 3 == selectedRatio) {
            onSelect(3)

        }
        BloodDonationRatioItem(value = 6, isSelected = 6 == selectedRatio) {
            onSelect(6)
        }
    }

}

@Composable
fun BloodDonationRatioItem(value: Int, isSelected: Boolean, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color.Red else Color.Black.copy(alpha = 0.1f))
            .clickableWithOutRipple { onSelect() }
            .padding(horizontal = 40.dp, vertical = 12.dp)
    ) {
        Text(
            text = "$value Months",
            textAlign = TextAlign.Center, color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

enum class BloodGroup(val value: String) {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"), B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-")
}

@Composable
fun BloodGroupDropdown(
    modifier: Modifier = Modifier,
    onBloodGroupSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBloodGroup by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(alpha = 0.1f))
            .padding(vertical = 15.dp, horizontal = 10.dp)
            .clickable { expanded = true }) {
        Text(
            text = selectedBloodGroup.ifEmpty { "Select Blood Group" },
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            BloodGroup.entries.forEach { bloodGroup ->
                DropdownMenuItem(text = { Text(text = bloodGroup.value) }, onClick = {
                    expanded = false
                    selectedBloodGroup = bloodGroup.value
                    onBloodGroupSelected(bloodGroup.value)
                })
            }
        }
    }
}

@Preview
@Composable
fun DonorScreenPreview() {
    DonorScreenContent(actionBecomeDonor = { _, _, _, _, _, _,_ -> }) {}
}