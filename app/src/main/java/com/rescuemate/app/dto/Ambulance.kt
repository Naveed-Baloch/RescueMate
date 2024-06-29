package com.rescuemate.app.dto

data class Ambulance(
    val ownerId: String,
    val lat: String,
    val lng: String,
    val vehicleNumber: String,
    val licenceNumber: String,
    val city: String,
    val isAvailableForSchedule: Boolean
)
