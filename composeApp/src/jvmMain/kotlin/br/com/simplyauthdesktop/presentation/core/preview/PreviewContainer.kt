package br.com.simplyauthdesktop.presentation.core.preview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import br.com.simplyauthdesktop.presentation.core.theme.AppTheme

/**
 * Container padrão para previews no Compose Desktop.
 * Garante:
 * - Tema correto (light/dark)
 * - Surface de fundo
 * - AppTheme aplicado
 * - Sem dependência de Koin
 */
@Composable
fun PreviewContainer(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    AppTheme(useDarkTheme = darkTheme) {
        Surface {
            content()
        }
    }
}

/**
 * Exemplo de uso com múltiplos previews
 */
@Preview
@Composable
private fun PreviewContainer_Light() {
    PreviewContainer(darkTheme = false) {
        // Coloque aqui um componente simples de teste
        Text("Preview Light")
    }
}

@Preview
@Composable
private fun PreviewContainer_Dark() {
    PreviewContainer(darkTheme = true) {
        Text("Preview Dark")
    }
}