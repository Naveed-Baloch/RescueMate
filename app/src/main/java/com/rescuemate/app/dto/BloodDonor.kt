package com.rescuemate.app.dto

data class BloodDonor(
    val userId: String,
    val bloodGroup: String,
    val lat: String,
    val lng: String,
    val isAvailable: String
)
