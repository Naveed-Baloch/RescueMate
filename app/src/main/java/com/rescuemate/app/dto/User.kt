package com.rescuemate.app.dto

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
    val name: String = "",
    val email: String = "",
    @get:Exclude val profileUri: String = "",
    val profilePicUrl: String = "",
    val cnic: String = "",
    val userType: UserType = UserType.Patient,
    val password: String = "",
    val token: String = "",
    val phoneNumber: String = "",
) : Parcelable {
    companion object
}


val User.Companion.mock by lazy {
    User(
        userId = "dicant",
        name = "Nelson Blanchard",
        email = "avis.nelson@example.com",
        profileUri = "pulvinar",
        profilePicUrl = "https://search.yahoo.com/search?p=usu",
        cnic = "faucibus",
        userType = UserType.Donor,
        password = "expetendis",
        phoneNumber = "(525) 159-7608"
    )
}