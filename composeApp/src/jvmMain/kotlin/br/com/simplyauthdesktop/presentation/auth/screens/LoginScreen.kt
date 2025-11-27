package br.com.simplyauthdesktop.presentation.auth.screens

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
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
import br.com.simplyauthdesktop.presentation.auth.AuthState
import br.com.simplyauthdesktop.presentation.auth.AuthViewModel
import br.com.simplyauthdesktop.presentation.auth.components.AuthButton
import br.com.simplyauthdesktop.presentation.auth.components.AuthTextField
import br.com.simplyauthdesktop.presentation.core.theme.ThemeSwitch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onForgotPassword: () -> Unit,
    onThemeToggle: () -> Unit,
    isDarkMode: Boolean,
    viewModel: AuthViewModel = koinInject()
) {
    // Reset state ons start screen (avoid dirt state of older navigatiuon)
    LaunchedEffect(Unit) {
        viewModel.reset()
    }

    // Consume StateFlow correctly
    val uiState by viewModel.uiState.collectAsState()

    // Create FocusRequesters to manager focus between fields
    val phoneFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Simulation: deactivate real login real on preview
    val isPreview = LocalInspectionMode.current

    // Real time validation
    val emailError = remember(email) { email.isNotBlank() && !isValidEmail(email) }
    val passwordError = remember(password) { password.isNotBlank() && !isValidPassword(password) }

    var hasFocused by remember { mutableStateOf(false) }
    var isLayoutReady by remember { mutableStateOf(false) }

    // No AuthTextField modifier:
    LaunchedEffect(isLayoutReady) {
        if (isLayoutReady && !isPreview) {
            phoneFocusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Login") },
                actions = {
                    ThemeSwitch(
                        isDark = isDarkMode,
                        onToggle = onThemeToggle,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .fillMaxSize()
                .focusGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ---------- Login Field (email) ----------
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email address",
                leadingIcon = Icons.Default.Person,
                isError = emailError,
                errorMessage = if (emailError) "Invalid email address" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                ),
                focusRequester = phoneFocusRequester,
                onTabPressed = { passwordFocusRequester.requestFocus() },
                modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                    if (!hasFocused && !isPreview && layoutCoordinates.isAttached && layoutCoordinates.size.width > 0) {
                        hasFocused = true
                        phoneFocusRequester.requestFocus()
                    }
                    isLayoutReady = true
                }
            )

            Spacer(Modifier.height(16.dp))

            // ---------- Password Field ----------
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Password,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide" else "Show"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError,
                errorMessage = if (passwordError) "Invalid password" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isPreview && email.isNotBlank() && password.isNotBlank()) {
                            viewModel.login(email, password)
                        }
                    }
                ),
                focusRequester = if (isPreview) null else passwordFocusRequester,
                onTabPressed = null,
                modifier = Modifier
            )

            Spacer(Modifier.height(24.dp))

            // ---------- Entry Button ----------
            AuthButton(
                text = "Enter",
                onClick = { viewModel.login(email, password) },
                enabled = (email.isNotBlank() && password.isNotBlank()) && (!emailError && !passwordError)
            )

            Spacer(Modifier.height(16.dp))

            // ---------- Links (Forgot Password / Register) ----------
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text(
                        "Forgot Password",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(onClick = onRegisterClick) {
                    Text(
                        "Sing Up",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // ---------- States ----------
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
                is AuthState.Success -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Login successful",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                else -> Unit
            }
        }
    }
}

// Validation functions
private fun isValidEmail(email: String): Boolean =
    email.isNotBlank() && Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)

private fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}