package br.com.simplyauthdesktop.presentation.auth.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import br.com.simplyauthdesktop.presentation.auth.screens.ForgotPasswordScreen
import br.com.simplyauthdesktop.presentation.core.preview.PreviewContainer

@Preview
@Composable
fun ForgotPasswordScreenPreview_Light() {
    val viewModel = remember { AuthPreviewFactory.createViewModel() }

    // Simula chamada de recuperação para demonstrar estado
    LaunchedEffect(Unit) {
        viewModel.recoverPassword("11999999999")
    }

    PreviewContainer(darkTheme = false) {
        ForgotPasswordScreen(
            onLoginClick = {},
            onRegisterClick = {},
            viewModel = viewModel
        )
    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview_Dark() {
    val viewModel = remember { AuthPreviewFactory.createViewModel() }

    LaunchedEffect(Unit) {
        viewModel.recoverPassword("teste@exemplo.com")
    }

    PreviewContainer(darkTheme = true) {
        ForgotPasswordScreen(
            onLoginClick = {},
            onRegisterClick = {},
            viewModel = viewModel
        )
    }
}