package com.rescuemate.app.dto

data class Ambulance(
    val ownerId: String,
    val lat: String,
    val lng: String,
    val modal: String,
    val number: String,
    val city: String,
    val scheduleUntil: String,
    val isAvailableForSchedule: Boolean,
    val images: List<String>
)
