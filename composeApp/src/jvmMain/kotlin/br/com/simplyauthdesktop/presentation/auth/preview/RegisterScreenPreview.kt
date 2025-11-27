package br.com.simplyauthdesktop.presentation.auth.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.simplyauthdesktop.presentation.auth.screens.RegisterScreen
import br.com.simplyauthdesktop.presentation.core.preview.PreviewContainer

@Preview
@Composable
fun RegisterScreenPreview_Light() {
    val viewModel = remember { AuthPreviewFactory.createViewModel() }
    var isDark by remember { mutableStateOf(false) }

    PreviewContainer(darkTheme = isDark) {
        RegisterScreen(
            onLoginClick = {},
            onForgotPassword = {},
            viewModel = viewModel
        )
    }
}

@Preview
@Composable
fun RegisterScreenPreview_Dark() {
    val viewModel = remember { AuthPreviewFactory.createViewModel() }
    var isDark by remember { mutableStateOf(true) }

    PreviewContainer(darkTheme = isDark) {
        RegisterScreen(
            onLoginClick = {},
            onForgotPassword = {},
            viewModel = viewModel
        )
    }
}