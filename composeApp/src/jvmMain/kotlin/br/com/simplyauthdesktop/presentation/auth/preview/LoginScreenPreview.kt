package br.com.simplyauthdesktop.presentation.auth.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.simplyauthdesktop.presentation.auth.screens.LoginScreen
import br.com.simplyauthdesktop.presentation.core.preview.PreviewContainer

@Preview
@Composable
fun LoginScreenPreview_Light() {
    val viewModel = remember { AuthPreviewFactory.createViewModel() }
    var isDark by remember { mutableStateOf(false) }
    val toggleTheme = { isDark = !isDark }

    PreviewContainer(darkTheme = isDark) {
        LoginScreen(
            onRegisterClick = {},
            onForgotPassword = {},
            onThemeToggle = toggleTheme,
            isDarkMode = isDark,
            viewModel = viewModel
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview_Dark() {
    val viewModel = remember { AuthPreviewFactory.createViewModel() }
    var isDark by remember { mutableStateOf(true) }
    val toggleTheme = { isDark = !isDark }

    PreviewContainer(darkTheme = isDark) {
        LoginScreen(
            onRegisterClick = {},
            onForgotPassword = {},
            onThemeToggle = toggleTheme,
            isDarkMode = isDark,
            viewModel = viewModel
        )
    }
}