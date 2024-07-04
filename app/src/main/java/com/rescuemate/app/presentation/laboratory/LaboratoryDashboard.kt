package com.rescuemate.app.presentation.laboratory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.R
import com.rescuemate.app.dto.User
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.openEmail
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.LaboratoryVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.ActionButton

@Composable
fun LaboratoryDashBoardScreen(user: User, navHostController: NavHostController) {
    LaboratoryDashBoardScreenContent(
        user = user,
        actionAddLab = {
            navHostController.navigate(Routes.LaboratoryScreen)
        },
        actionAddTest = {
            navHostController.navigate(Routes.TestsScreen)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LaboratoryDashBoardScreenContent(user: User, laboratoryVM: LaboratoryVM = hiltViewModel(), actionAddLab: () -> Unit, actionAddTest: () -> Unit) {
    val context = LocalContext.current
    val progressBar = remember { context.progressBar() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        isLoading = true
        progressBar.show()
        laboratoryVM.getUserLaboratory().collect {
            if (it is Result.Failure) {
                context.showToast(it.exception.message ?: "Something went Wrong")
            }
            if (it !is Result.Loading) {
                isLoading = false
                progressBar.isVisible(false)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome,\n${user.name}".uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = primaryColor,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 60.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (isLoading) 0f else 1f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            if (laboratoryVM.user == null) {
                ActionButton(
                    imageId = R.drawable.ic_emergency,
                    description = if (laboratoryVM.user == null) "Add Laboratory" else "Edit Laboratory",
                    imageModifier = Modifier.run { size(50.dp).offset(y = (-10).dp) },
                    onClick = actionAddLab
                )
            }

            if (laboratoryVM.userLab != null) {
                ActionButton(
                    imageId = R.drawable.ic_emergency,
                    description = "Laboratory Tests",
                    imageModifier = Modifier.run { size(50.dp).offset(y = (-10).dp) },
                    onClick = actionAddTest
                )
            }

            ActionButton(
                imageId = R.drawable.ic_rescue,
                description = "Need Help?\n",
                onClick = {
                    context.openEmail("ucphassan@gmail.com")
                }
            )

        }
    }
}