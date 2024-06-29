package com.rescuemate.app.presentation.auth

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bkcoding.garagegurufyp_user.utils.isValidEmail
import com.rescuemate.app.R
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.UserTypeDropdown

@Composable
fun SignUpScreen() {
    SignUpScreenContent(
        onSignIn = {},
        onSignUp = {}
    )
}

@Composable
private fun SignUpScreenContent(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var profilePicUri by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 30.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
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
            if(profilePicUri.isEmpty()) {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
            } else {
                // Profile Pic
            }

        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Name",
            style = MaterialTheme.typography.titleLarge.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomEditText(
            value = email,
            label = "Enter your name",
            isError = email.isNotEmpty() && !isValidEmail(email),
            onValueChange = {
                email = it
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleLarge.copy(Color.Black.copy(alpha = 0.7f)),
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

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.titleLarge.copy(Color.Black.copy(alpha = 0.7f)),
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
            style = MaterialTheme.typography.titleLarge.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))

        UserTypeDropdown(
            modifier = Modifier.fillMaxWidth(),onUserTypeSelected = {
        })
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
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Have an account? SignIn",
            style = MaterialTheme.typography.titleLarge.copy(Color.Red),
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    RescueMateTheme {
        SignUpScreen()
    }
}