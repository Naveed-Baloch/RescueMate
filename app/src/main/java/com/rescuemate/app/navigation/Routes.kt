package com.rescuemate.app.navigation

import android.os.Parcelable
import com.rescuemate.app.dto.User
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

object Routes {
    @Serializable
    object SplashScreen

    @Serializable
    object SignInScreen

    @Serializable
    object SignUpScreen

    @Serializable
    data class DashBoardScreen (val user: User)

    @Serializable
    data class BloodDonorsScreen(val donorSearchRequestParams: DonorSearchRequestParams)

    @Serializable
    object BloodDonorScreen

    @Serializable
    object BloodRequestScreen


    @Serializable
    object LaboratoryScreen

    @Serializable
    data class LaboratoriesScreen(val city: String, val laboratoryTest: String)

    @Serializable
    object LaboratoryRequestScreen


    @Serializable
    object TestsScreen

    @Serializable
    data class AmbulanceScreen(val user: User)

    @Serializable
    object AmbulanceRequestScreen

    @Serializable
    object AmbulanceRequestsScreen

    @Serializable
    data class TestScreen(val id: String)

}

@Serializable
@Parcelize
data class DonorSearchRequestParams(
    val bloodGroup: String,
    val city: String,
) : Parcelable