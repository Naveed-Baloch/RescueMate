package com.rescuemate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rescuemate.app.presentation.SplashScreen
import com.rescuemate.app.presentation.theme.RescueMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RescueMateTheme {
                SplashScreen()
            }
        }
    }
}