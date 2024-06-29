package com.rescuemate.app.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class UserType {
    Patient, AmbulanceOwner, Donor, LaboratoryOwner;

    fun getText() = when (this) {
        Patient -> "Patient"
        AmbulanceOwner -> "Ambulance Owner"
        Donor -> "Blood Donor"
        LaboratoryOwner -> "Laboratory Owner"
    }
}

@Parcelize
data class User(
    val userId: String,
    val name: String,
    val email: String,
    val city: String,
    val userType: UserType,
    val phoneNumber: String
) : Parcelable