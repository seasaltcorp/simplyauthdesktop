package br.com.simplyauthdesktop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.Alignment
import br.com.simplyauthdesktop.presentation.AuthNavigation
import br.com.simplyauthdesktop.presentation.core.theme.AppTheme
import org.koin.core.context.startKoin

fun main() = application {
    startKoin { modules(appModule) }

    var isDarkTheme by remember { mutableStateOf(false) } // ← ESTADO GLOBAL
    val toggleTheme = { isDarkTheme = !isDarkTheme }

    // Define o tamanho inicial da janela (ajuste conforme seu design)
    val windowState = rememberWindowState(
        width = 680.dp,
        height = 680.dp,
        position = WindowPosition.Aligned(Alignment.Center),
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Simplysched - Autenticação",
        state = windowState,
        resizable = true
    ) {

        AppTheme(useDarkTheme = isDarkTheme) {
            AuthNavigation(
                isDarkTheme = isDarkTheme,
                onThemeToggle = toggleTheme
            )
        }
    }
}