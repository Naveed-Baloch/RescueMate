package com.rescuemate.app.dto

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

enum class UserType {
    Patient, AmbulanceOwner, Donor, LaboratoryOwner;

    fun getText() = when (this) {
        Patient -> "Patient"
        AmbulanceOwner -> "Ambulance Owner"
        Donor -> "Blood Donor"
        LaboratoryOwner -> "Laboratory Owner"
    }
}
@Serializable
@Parcelize
data class User(
    val userId: String = "",
    val name: String,
    val email: String,
    @get:Exclude val profileUri: String,
    val profilePicUrl: String = "",
    val cnic: String = "",
    val userType: UserType,
    val password: String,
    val phoneNumber: String
) : Parcelable