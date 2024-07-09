package com.rescuemate.app.utils

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailRegex)
}

fun String.encode() = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

fun String.decode() = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())

enum class City {
    Lahore,
    Islamabad,
    Karachi,
    Multan
}

@Composable
fun CityDropDown(
    modifier: Modifier = Modifier,
    onCitySelected: (String) -> Unit,
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
            text = selectedBloodGroup.ifEmpty { "Select you city" },
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
            City.entries.forEach { city ->
                DropdownMenuItem(text = { Text(text = city.name) }, onClick = {
                    expanded = false
                    selectedBloodGroup = city.name
                    onCitySelected(city.name)
                })
            }
        }
    }
}

enum class BloodTest(val displayName: String) {
    CBC("Complete Blood Count (CBC)"),
    BMP("Basic Metabolic Panel (BMP)"),
    CMP("Comprehensive Metabolic Panel (CMP)"),
    Lipid("Lipid Panel"),
    LFTs("Liver Function Tests (LFTs)"),
    TFTs("Thyroid Function Tests (TFTs)"),
    HbA1c("Hemoglobin A1c (HbA1c)"),
    FBS("Fasting Blood Sugar (FBS)"),
    OGTT("Oral Glucose Tolerance Test (OGTT)"),
    Urinalysis("Urinalysis"),
    ECG("Electrocardiogram (ECG/EKG)"),
    ChestXray("Chest X-ray"),
    MRI("Magnetic Resonance Imaging (MRI)"),
    CTScan("Computed Tomography (CT) Scan"),
    Ultrasound("Ultrasound"),
    Mammogram("Mammogram"),
    PapSmear("Pap Smear"),
    PSA("Prostate-Specific Antigen (PSA) Test"),
    Colonoscopy("Colonoscopy"),
    DEXA("Bone Density Scan (DEXA)")
}

@Composable
fun LaboratoryTestDropDown(
    modifier: Modifier = Modifier,
    onBloodTestSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBloodTest by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(alpha = 0.1f))
            .padding(vertical = 15.dp, horizontal = 10.dp)
            .clickable { expanded = true }) {
        Text(
            text = selectedBloodTest.ifEmpty { "Select your blood test" },
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
            BloodTest.values().forEach { bloodTest ->
                DropdownMenuItem(text = { Text(text = bloodTest.displayName) }, onClick = {
                    expanded = false
                    selectedBloodTest = bloodTest.displayName
                    onBloodTestSelected(bloodTest.displayName)
                })
            }
        }
    }
}

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}