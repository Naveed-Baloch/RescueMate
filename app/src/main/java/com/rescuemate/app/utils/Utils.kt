package com.rescuemate.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.TimeZone

fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailRegex)
}

fun isValidText(text: String): Boolean {
    return text.matches(Regex("[a-zA-Z ]+"))
}


fun getInboxRelativeTime(firebaseTime: String): String {
    // Parse the API timestamp
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val apiDateTime = LocalDateTime.parse(firebaseTime, formatter)

    // Get the current time
    val currentDateTime = LocalDateTime.now(ZoneId.of(TimeZone.getDefault().id))

    // Calculate the difference
    val years = ChronoUnit.YEARS.between(apiDateTime, currentDateTime)
    val months = ChronoUnit.MONTHS.between(apiDateTime, currentDateTime)
    val weeks = ChronoUnit.WEEKS.between(apiDateTime, currentDateTime)
    val days = ChronoUnit.DAYS.between(apiDateTime, currentDateTime)
    val hours = ChronoUnit.HOURS.between(apiDateTime, currentDateTime)
    val minutes = ChronoUnit.MINUTES.between(apiDateTime, currentDateTime)

    return when {
        years > 0 -> "${years}y"
        months > 0 -> "${months}m"
        weeks > 0 -> "${weeks}w"
        days > 0 -> "${days}d"
        hours > 0 -> "${hours}h"
        minutes > 0 -> "${minutes}min"
        else -> "just now"
    }
}

fun Context.openDialPanel(phoneNumber: String){
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    startActivity(intent)
}


fun Context.openWhatsApp(phoneNumber: String) {
    val url = "https://wa.me/$phoneNumber"
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        setPackage("com.whatsapp")
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        // Handle the case where WhatsApp is not installed
        Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
    }
}


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