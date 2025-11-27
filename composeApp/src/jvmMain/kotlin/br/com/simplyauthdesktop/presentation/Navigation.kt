package br.com.simplyauthdesktop.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import br.com.simplyauthdesktop.presentation.auth.screens.ForgotPasswordScreen
import br.com.simplyauthdesktop.presentation.auth.screens.LoginScreen
import br.com.simplyauthdesktop.presentation.auth.screens.RegisterScreen

enum class AuthRoute { Login, Register, ForgotPassword }

@Composable
fun AuthNavigation(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val currentRoute = remember { mutableStateOf(AuthRoute.Login) }

    when (currentRoute.value) {
        AuthRoute.Login -> LoginScreen(
            onRegisterClick = {
                currentRoute.value = AuthRoute.Register
            },
            onForgotPassword = {
                currentRoute.value = AuthRoute.ForgotPassword
            },
            onThemeToggle = onThemeToggle,
            isDarkMode = isDarkTheme
        )

        AuthRoute.Register -> RegisterScreen(
            onLoginClick = {
                currentRoute.value = AuthRoute.Login
            },
            onForgotPassword = {
                currentRoute.value = AuthRoute.ForgotPassword
            }
        )

        AuthRoute.ForgotPassword -> ForgotPasswordScreen(
            onLoginClick = {
                currentRoute.value = AuthRoute.Login
            },
            onRegisterClick = {
                currentRoute.value = AuthRoute.Register
            }
        )
    }
}