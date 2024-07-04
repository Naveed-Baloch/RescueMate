package com.rescuemate.app.presentation.laboratory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.blooddonor.BloodGroupDropdown
import com.rescuemate.app.utils.CityDropDown
import com.rescuemate.app.utils.LaboratoryTestDropDown
import com.rescuemate.app.utils.TopBar

@Composable
fun LaboratoryRequest(navHostController: NavHostController) {
    LaboratoryRequestScreenContent(
        findLaboratory = { city , laboratoryTest->
            navHostController.navigate(Routes.LaboratoriesScreen(city, laboratoryTest))
        },
        onBack = { navHostController.popBackStack() }
    )
}

@Composable
fun LaboratoryRequestScreenContent(findLaboratory: (city: String, laboratoryTest: String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    var city by remember { mutableStateOf("") }
    var laboratoryTest by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 45.dp)
    ) {
        TopBar(modifier = Modifier.padding(horizontal = 20.dp), text = "Find Test Center") {
            onBack()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
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
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Select Test",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            LaboratoryTestDropDown(
                modifier = Modifier.fillMaxWidth(),
                onBloodTestSelected = { laboratoryTest = it }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Red)
                    .clickableWithOutRipple {
                        if (city.isNotEmpty() && laboratoryTest.isNotEmpty()) {
                            findLaboratory(city, laboratoryTest)
                        } else {
                            context.showToast("Something is missing!")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Find".uppercase(),
                    color = Color.White, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun BloodRequestScreenContentPreview() {
    LaboratoryRequestScreenContent(onBack = {}, findLaboratory = { _, _-> })
}