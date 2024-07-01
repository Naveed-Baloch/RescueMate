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


}