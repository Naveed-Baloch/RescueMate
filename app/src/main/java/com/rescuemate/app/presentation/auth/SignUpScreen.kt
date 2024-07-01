package com.rescuemate.app.presentation.auth

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.rememberAsyncImagePainter
import com.rescuemate.app.utils.isValidEmail
import com.rescuemate.app.R
import com.rescuemate.app.dto.User
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.navigation.Routes
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.presentation.viewmodel.UserStorageVM
import com.rescuemate.app.presentation.viewmodel.UserViewModel
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.UserTypeDropdown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navHostController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
    userStorageVM: UserStorageVM = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val progressBar = remember { context.progressBar() }
    var keepShowingLoading by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = userViewModel.isLoading) {
        if(!userViewModel.isLoading) delay(1000)
        keepShowingLoading = userViewModel.isLoading
    }

    LaunchedEffect(key1 = keepShowingLoading) {
        if(keepShowingLoading) {
            progressBar.show()
        } else {
            progressBar.dismiss()
        }
    }

    SignUpScreenContent(
        onSignIn = {
            navHostController.popBackStack()
        },
        onSignUp = { user ->
            signup(
                scope = scope,
                user = user,
                context = context,
                userVM = userViewModel,
            ) { updatedUser->
                userStorageVM.setUser(updatedUser)
                keepShowingLoading = false
                Log.d("SignUpScreen", "SignUpScreen: with user: $updatedUser")
                navHostController.navigate(Routes.DashBoardScreen) {
                    popUpTo(navHostController.graph.id)
                }
            }
        }
    )
}

@Composable
private fun SignUpScreenContent(onSignIn: () -> Unit, onSignUp: (User) -> Unit) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }
    var password by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf<UserType?>(null) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        profilePicUri = it
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_rescue),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "Create Your Account!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(90.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (profilePicUri == null) {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clickableWithOutRipple {
                            galleryLauncher.launch("image/*")
                        }
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(profilePicUri),
                    contentDescription = null, contentScale = ContentScale.Crop
                )
            }

        }
        Text(
            text = "Upload your profile pic",
            style = MaterialTheme.typography.titleSmall.copy(Color.Black.copy(alpha = 0.7f)),
        )
        Text(
            text = "Name",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
        )
        CustomEditText(
            value = name,
            label = "Enter your name",
            isError = name.isNotEmpty(),
            onValueChange = {
                name = it
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
        )
        CustomEditText(
            value = email,
            label = "Enter your email",
            isError = email.isNotEmpty() && !isValidEmail(email),
            onValueChange = {
                email = it
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Phone Number",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
        )
        CustomEditText(
            value = phoneNumber,
            keyboardType = KeyboardType.Phone,
            label = "Enter your phone number",
            isError = phoneNumber.isNotEmpty(),
            onValueChange = {
                phoneNumber = it
            }
        )

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
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
                        Image(
                            painter = painterResource(id = R.drawable.ic_show),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                    } else
                        Image(
                            painter = painterResource(id = R.drawable.ic_hide),
                            contentDescription = ""
                        )
                }
            },
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Select you are",
            style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )

        UserTypeDropdown(
            modifier = Modifier.fillMaxWidth(),
            onUserTypeSelected = { userType = it }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign Up".uppercase(),
                color = Color.White, fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clickableWithOutRipple {
                        val isValidInput = isValidInput(
                            profilePicUri,
                            userType,
                            context,
                            name,
                            email,
                            password,
                            phoneNumber
                        )
                        if (isValidInput) {
                            val user = User(
                                name = name,
                                email = email,
                                profileUri = profilePicUri.toString(),
                                userType = userType!!,
                                password = password,
                                phoneNumber = phoneNumber
                            )
                            onSignUp(user)
                        }
                    }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Have an account? SignIn",
            style = MaterialTheme.typography.titleLarge.copy(Color.Red),
            modifier = Modifier.clickableWithOutRipple {
                onSignIn()
            }
        )
    }
}

private fun isValidInput(
    profilePicUri: Uri?,
    userType: UserType?,
    context: Context,
    name: String,
    email: String,
    password: String,
    phoneNumber: String
): Boolean {
    var isInputValid = false
    when {
        profilePicUri == null -> {
            context.showToast("Please upload your profile pic")
        }

        userType == null || name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() -> {
            context.showToast("Something is Missing")
        }

        !isValidEmail(email) -> context.showToast("Invalid Email")
        password.length < 6 -> context.showToast("Password too short")
        else -> isInputValid = true
    }
    return isInputValid
}

private fun signup(
    scope: CoroutineScope,
    user: User,
    context: Context,
    userVM: UserViewModel,
    onSignUp :(User) -> Unit
) {
    scope.launch {
        val uri = Uri.parse(user.profileUri)
        userVM.signup(user).collect{ signUpRes ->
            when(signUpRes) {
                is Result.Failure -> {
                    context.showToast(message = signUpRes.exception.message ?: "Something went wrong")
                }
                is Result.Success -> {
                    val userId = signUpRes.data
                    userVM.uploadUserProfile(uri, userId).collect { uploadImageRes ->
                        when(uploadImageRes) {
                            is Result.Failure -> {
                                context.showToast(message = uploadImageRes.exception.message ?: "Something went wrong")
                            }
                            is Result.Success -> {
                                val url = uploadImageRes.data
                                val updatedUser = user.copy(userId = userId, profilePicUrl = url)
                                userVM.storeUserToDb(updatedUser).collect{ storeUserRes ->
                                    when(storeUserRes) {
                                        is Result.Failure -> {
                                            context.showToast(message = storeUserRes.exception.message ?: "Something went wrong")
                                        }
                                        is Result.Success -> {
                                            context.showToast(message = "Your Account is Created!")
                                            onSignUp(updatedUser)
                                        }
                                        else -> {}
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
                else -> {}
            }
        }





    }

}


@Preview
@Composable
fun SignUpScreenPreview() {
    RescueMateTheme {
        SignUpScreenContent(onSignIn = {}, onSignUp = {})
    }
}