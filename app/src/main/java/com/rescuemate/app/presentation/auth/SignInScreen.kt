package com.rescuemate.app.presentation.auth

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.R
import com.rescuemate.app.dto.User
import com.rescuemate.app.dto.getEncodedUser
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.getActivity
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.presentation.viewmodel.UserStorageVM
import com.rescuemate.app.presentation.viewmodel.UserViewModel
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.isValidEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navHostController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
    userStorageVM: UserStorageVM = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var keepShowingLoading by remember { mutableStateOf(false) }
    val progressBar = remember { context.progressBar() }

    LaunchedEffect(key1 = userViewModel.isLoading) {
        if (!userViewModel.isLoading) delay(1000)
        keepShowingLoading = userViewModel.isLoading
        progressBar.isVisible(keepShowingLoading)
    }

    SignInScreenContent(
        onSignIn = { email, password ->
            scope.launch {
                userViewModel.login(email = email, password = password).collect {
                    when (it) {
                        is Result.Failure -> {
                            userStorageVM.removeUserData()
                            context.showToast(it.exception.message ?: "Something went Wrong")
                        }

                        is Result.Success -> {
                            val userId = it.data
                            fetchUserDetails(
                                scope = scope, userViewModel = userViewModel, context = context,
                                userId = userId,
                            ) { user ->
                                progressBar.dismiss()
                                userStorageVM.removeUserData()
                                userStorageVM.setUser(user = user)
                                val requestPayLoadId = userStorageVM.getPayloadRequestId()
                                if (!requestPayLoadId.isNullOrEmpty()) {
                                    navHostController.navigate(Routes.AmbulanceRequestDetailScreen(requestPayLoadId)){
                                        popUpTo(navHostController.graph.id)
                                    }
                                } else {
                                    navHostController.navigate(Routes.DashBoardScreen(user = user.getEncodedUser())) {
                                        popUpTo(navHostController.graph.id)
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        },
        onSignUp = {
            navHostController.navigate(Routes.SignUpScreen)
        }
    )

}

fun fetchUserDetails(
    scope: CoroutineScope,
    userViewModel: UserViewModel,
    context: Context,
    userId: String,
    onSuccessfulLogin: (User) -> Unit,
) {
    scope.launch {
        userViewModel.fetchUserDetails(userId = userId).collect {
            when (it) {
                is Result.Failure -> {
                    context.showToast(it.exception.message ?: "Something went Wrong")
                }

                is Result.Success -> {
                    onSuccessfulLogin(it.data)
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun SignInScreenContent(
    onSignIn: (String, String) -> Unit,
    onSignUp: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_rescue),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Sign In Your Account!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = email,
            label = "Enter your email",
            isError = email.isNotEmpty() && !isValidEmail(email),
            onValueChange = {
                email = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomEditText(
            value = password,
            label = "Enter your password",
            visualTransformation = if (passwordVisibility) VisualTransformation.Companion.None else PasswordVisualTransformation(),
            isError = password.isNotEmpty(),
            onValueChange = { password = it },
            keyboardType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    if (passwordVisibility) {
                        Image(painter = painterResource(id = R.drawable.ic_show), contentDescription = "", modifier = Modifier.size(25.dp))
                    } else
                        Image(painter = painterResource(id = R.drawable.ic_hide), contentDescription = "")
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red)
                .clickableWithOutRipple {
                    if (password.isNotEmpty() && isValidEmail(email)) {
                        onSignIn(email, password)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign In".uppercase(),
                color = Color.White, fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Don't have a account? SignUp",
            style = MaterialTheme.typography.titleLarge.copy(Color.Red), textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithOutRipple {
                    onSignUp()
                }
        )
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    RescueMateTheme {
        SignInScreenContent(onSignIn = { _, _ -> }, onSignUp = {})
    }
}