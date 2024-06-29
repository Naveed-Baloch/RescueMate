package com.rescuemate.app.dto

data class BloodDonor(
    val userId: String,
    val bloodGroup: String,
    val howOftenCanDonate: String,
    val medicalHistory: String,
    val isAvailable: String
)
