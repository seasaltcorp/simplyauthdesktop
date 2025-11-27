package br.com.simplyauthdesktop.presentation.auth.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.simplyauthdesktop.presentation.auth.AuthState
import br.com.simplyauthdesktop.presentation.auth.AuthViewModel
import br.com.simplyauthdesktop.presentation.auth.components.AuthButton
import br.com.simplyauthdesktop.presentation.auth.components.AuthTextField
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: AuthViewModel = koinInject()
) {
    // Reset do ViewModel ao entrar na tela
    LaunchedEffect(Unit) { viewModel.reset() }

    val uiState by viewModel.uiState.collectAsState()

    // FocusRequester para o campo de identificador
    val identifierFocus = remember { FocusRequester() }

    var identifier by remember { mutableStateOf("") }

    // Validação em tempo real
    val identifierError = remember(identifier) {
        identifier.isNotBlank() && !isValidIdentifier(identifier)
    }

    val isPreview = LocalInspectionMode.current

    // Foco inicial
    LaunchedEffect(Unit) {
        if (!isPreview) identifierFocus.requestFocus()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Recover Password") },
                navigationIcon = {
                    IconButton(onClick = onLoginClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Campo de identificador (telefone ou e‑mail)
            AuthTextField(
                value = identifier,
                onValueChange = { identifier = it },
                label = "Phone number or Email address",
                leadingIcon = Icons.Default.Email,
                isError = identifierError,
                errorMessage = if (identifierError) "Invalid identifier" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isPreview && identifier.isNotBlank() && !identifierError) {
                            viewModel.recoverPassword(identifier)
                        }
                    }
                ),
                focusRequester = identifierFocus,
                onTabPressed = null
            )

            Spacer(Modifier.height(24.dp))

            // Botão Enviar Token
            AuthButton(
                text = "Send Token",
                onClick = { viewModel.recoverPassword(identifier) },
                enabled = identifier.isNotBlank() && !identifierError
            )

            Spacer(Modifier.height(16.dp))

            // Link Voltar
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onLoginClick) {
                    Text(
                        "Log In",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(onClick = onRegisterClick) {
                    Text(
                        "Sign Up",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // Estados da operação
            when (uiState) {
                is AuthState.Loading -> {
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
                is AuthState.Error -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        (uiState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is AuthState.RecoverySuccess -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        (uiState as AuthState.RecoverySuccess).message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                else -> Unit
            }
        }
    }
}

// Funções de validação
private fun isValidIdentifier(text: String): Boolean =
    isValidPhone(text) || isValidEmail(text)

private fun isValidPhone(phone: String): Boolean =
    phone.matches(Regex("""^(55)?[1-9]\d{9,10}$"""))

private fun isValidEmail(email: String): Boolean =
    email.isNotBlank() && Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)