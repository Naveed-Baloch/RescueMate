package com.bkcoding.garagegurufyp_user.utils

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

enum class City {
    All,
    Lahore,
    Islamabad,
    Karachi,
    Multan
}