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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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

@Composable
fun SignInScreen() {
    SignInScreenContent(
        onSign = {},
        onSignUp = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScreenContent(
    onSign: () -> Unit,
    onSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 30.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
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
            style = MaterialTheme.typography.titleLarge.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomEditText(
            value = email,
            label = "Enter your email",
            isError = email.isNotEmpty() && !isValidEmail(email),
            onValueChange = {
                email = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.titleLarge.copy(Color.Black.copy(alpha = 0.7f)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
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
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red),
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
            style = MaterialTheme.typography.titleLarge.copy(Color.Red),
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    RescueMateTheme {
        SignInScreen()
    }
}