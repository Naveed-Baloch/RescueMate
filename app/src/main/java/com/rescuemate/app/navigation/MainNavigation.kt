package com.rescuemate.app.navigation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rescuemate.app.dto.User
import com.rescuemate.app.extensions.getActivity
import com.rescuemate.app.presentation.SplashScreen
import com.rescuemate.app.presentation.ambulance.AmbulanceRequestDetailScreen
import com.rescuemate.app.presentation.ambulance.AmbulanceRequestScreen
import com.rescuemate.app.presentation.ambulance.AmbulanceRequestsScreen
import com.rescuemate.app.presentation.ambulance.AmbulanceScreen
import com.rescuemate.app.presentation.auth.SignInScreen
import com.rescuemate.app.presentation.auth.SignUpScreen
import com.rescuemate.app.presentation.blooddonor.BloodDonorScreen
import com.rescuemate.app.presentation.blooddonor.BloodDonorsScreen
import com.rescuemate.app.presentation.blooddonor.BloodRequestScreen
import com.rescuemate.app.presentation.dashboard.DashboardScreen
import com.rescuemate.app.presentation.laboratory.LaboratoriesScreen
import com.rescuemate.app.presentation.laboratory.TestsScreen
import com.rescuemate.app.presentation.laboratory.LaboratoryRequest
import com.rescuemate.app.presentation.laboratory.LaboratoryScreen
import kotlin.reflect.typeOf

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainNavigation(navController: NavHostController) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = remember { getStartDestination(context) }
    ) {
        composable<Routes.SplashScreen> {
            SplashScreen(navController)
        }

        composable<Routes.SignInScreen> {
            SignInScreen(navController)
        }

        composable<Routes.SignUpScreen> {
            SignUpScreen(navController)
        }

        composable<Routes.DashBoardScreen>(
            typeMap = mapOf(typeOf<User>() to CustomNavType(clazz = User::class.java, serializer = User.serializer()))
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.DashBoardScreen>()
            DashboardScreen(navHostController = navController, user = args.user)
        }

        composable<Routes.BloodDonorsScreen>(
            typeMap = mapOf(typeOf<DonorSearchRequestParams>() to CustomNavType(clazz = DonorSearchRequestParams::class.java, serializer = DonorSearchRequestParams.serializer()))
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.BloodDonorsScreen>()
            BloodDonorsScreen(navController = navController, donorSearchRequestParams = args.donorSearchRequestParams)
        }

        composable<Routes.BloodDonorScreen> {
            BloodDonorScreen(navHostController = navController)
        }

        composable<Routes.BloodRequestScreen> {
            BloodRequestScreen(navHostController = navController)
        }

        composable<Routes.LaboratoryScreen> {
            LaboratoryScreen(navHostController = navController)
        }

        composable<Routes.LaboratoriesScreen> { backStackEntry ->
            val args: Routes.LaboratoriesScreen = backStackEntry.toRoute()
            LaboratoriesScreen(navController = navController, city = args.city, laboratoryTest = args.laboratoryTest)
        }

        composable<Routes.LaboratoryRequestScreen> {
            LaboratoryRequest(navHostController = navController)
        }

        composable<Routes.TestsScreen> {
            TestsScreen(navController = navController)
        }

        composable<Routes.AmbulanceScreen>(
            typeMap = mapOf(typeOf<User>() to CustomNavType(clazz = User::class.java, serializer = User.serializer()))
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.AmbulanceScreen>()
            AmbulanceScreen(navHostController = navController, user = args.user)
        }

        composable<Routes.AmbulanceRequestScreen> {
            AmbulanceRequestScreen(navHostController = navController)
        }

        composable<Routes.AmbulanceRequestsScreen> {
            AmbulanceRequestsScreen(navController = navController)
        }

        composable<Routes.AmbulanceRequestDetailScreen>(
//            deepLinks = listOf(navDeepLink { uriPattern = "https://rescuemate/request/{id}" })
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.AmbulanceRequestDetailScreen>()
            AmbulanceRequestDetailScreen(requestId = args.id, navHostController = navController)
        }

    }
}


private fun getStartDestination(context: Context): Any {
    val payLoadRequestId = context.getActivity()?.intent?.extras?.getString("requestId")
    return if(!payLoadRequestId.isNullOrEmpty()) Routes.AmbulanceRequestDetailScreen(payLoadRequestId) else Routes.SplashScreen
}