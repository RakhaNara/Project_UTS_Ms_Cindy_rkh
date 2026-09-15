package com.example.projectutsmscindy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectutsmscindy.ui.LoginScreen
import com.example.projectutsmscindy.ui.LoanViewModel
import com.example.projectutsmscindy.ui.MainScreen
import com.example.projectutsmscindy.ui.theme.ProjectUTSMsCindyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        setContent {
            ProjectUTSMsCindyTheme {
                var isLoggedIn by remember {
                    mutableStateOf(sharedPref.getBoolean("is_logged_in", false))
                }

                if (isLoggedIn) {
                    val viewModel: LoanViewModel = viewModel()
                    MainScreen(viewModel = viewModel)
                } else {
                    LoginScreen(
                        onLoginSuccess = {
                            sharedPref.edit().putBoolean("is_logged_in", true).apply()
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }
}