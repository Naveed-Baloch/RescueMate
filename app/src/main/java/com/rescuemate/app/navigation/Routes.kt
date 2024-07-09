package com.rescuemate.app.navigation

import android.os.Parcelable
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
    object DashBoardScreen

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
    object AmbulanceScreen

    @Serializable
    object AmbulanceRequestScreen

    @Serializable
    object AmbulanceRequestsScreen

}

@Serializable
@Parcelize
data class DonorSearchRequestParams(
    val bloodGroup: String,
    val city: String,
) : Parcelable