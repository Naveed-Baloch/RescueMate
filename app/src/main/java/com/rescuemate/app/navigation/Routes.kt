package com.rescuemate.app.navigation

import com.rescuemate.app.dto.User
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
    data class BloodDonorsScreen(val bloodGroup: String, val city: String)

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