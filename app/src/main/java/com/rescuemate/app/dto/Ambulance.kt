package com.rescuemate.app.dto

data class Ambulance(
    val ownerId: String = "",
    val vehicleNumber: String = "",
    val licenceNumber: String = "",
    val city: String = "",
    val isAvailable: Boolean = true
)
