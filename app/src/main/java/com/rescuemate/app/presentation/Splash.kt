package com.rescuemate.app.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.R
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.viewmodel.UserStorageVM
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    userStorageVM: UserStorageVM = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        val user = userStorageVM.getUser()
        delay(1000)
        if (user == null) {
            navHostController.navigate(Routes.SignInScreen) {
                popUpTo(navHostController.graph.id)
            }
        } else {
            navHostController.navigate(Routes.DashBoardScreen(user = user)) {
                popUpTo(navHostController.graph.id)
            }
        }
    }
    SplashScreenContent()
}

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Color.Black, Color.Red)))
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_rescue),
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp)
            )
            Text(
                text = "Rescue\nMate".uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White, textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = (-70).dp)
            )
        }

        Text(
            text = "Emergency \nNon Emergency Services",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White, textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 70.dp, horizontal = 50.dp)
        )

    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreenContent()
}