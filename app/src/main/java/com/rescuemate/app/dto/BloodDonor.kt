package com.rescuemate.app.dto

data class BloodDonor(
    val userId: String = "",
    val name: String = "",
    val dob: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val profilePicUrl: String = "",
    val bloodGroup: String = "",
    val address: String = "",
    val howOftenCanDonate: Int = 0,
    val medicalHistory: String = "",
    val isAvailable: Boolean = false,
)
