package br.com.simplyauthdesktop.presentation.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.simplyauthdesktop.presentation.auth.AuthState
import br.com.simplyauthdesktop.presentation.auth.AuthViewModel
import br.com.simplyauthdesktop.presentation.auth.components.AuthButton
import br.com.simplyauthdesktop.presentation.auth.components.AuthTextField
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: AuthViewModel = koinInject()
) {
    // Reset estado ao entrar na tela
    LaunchedEffect(Unit) {
        viewModel.reset()
    }

    // Consumir StateFlow corretamente
    val uiState by viewModel.uiState.collectAsState()

    // FocusRequesters
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isPreview = LocalInspectionMode.current

    // Validações em tempo real
    val namerError = remember(name) { name.isNotBlank() && name.length > 3 }
    val emailError = remember(email) { email.isNotBlank() && !isValidEmail(email) }
    val phoneError = remember(phone) { phone.isNotBlank() && !isValidPhoneWithCountry(phone) }
    val passwordError = remember(password) { password.isNotBlank() && !isValidPassword(password) }
    val confirmError = remember(confirmPassword, password) { confirmPassword.isNotBlank() && confirmPassword != password }

    val formValid = remember(email, phone, password, confirmPassword) {
        isValidEmail(email) && isValidPhoneWithCountry(phone) && isValidPassword(password) && password == confirmPassword
    }

    // Foco inicial
    LaunchedEffect(Unit) {
        if (!isPreview) {
            emailFocus.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sign Up") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .fillMaxSize()   // <--- ÚNICA LINHA ALTERADA: .fillMaxWidth() → .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Name
            AuthTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full name",
                leadingIcon = Icons.Default.Person,
                isError = namerError,
                errorMessage = if (namerError) "Invalid name" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
                focusRequester = nameFocus,
                onTabPressed = { emailFocus.requestFocus() }
            )

            Spacer(Modifier.height(16.dp))

            // Email
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email address",
                leadingIcon = Icons.Default.Email,
                isError = emailError,
                errorMessage = if (emailError) "Invalid email" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { phoneFocus.requestFocus() }),
                focusRequester = emailFocus,
                onTabPressed = { phoneFocus.requestFocus() }
            )

            Spacer(Modifier.height(16.dp))

            // Telefone com código do país
            AuthTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone number (+55)",
                leadingIcon = Icons.Default.Phone,
                isError = phoneError,
                errorMessage = if (phoneError) "Ex: 5511999999999" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                focusRequester = phoneFocus,
                onTabPressed = { passwordFocus.requestFocus() }
            )

            Spacer(Modifier.height(16.dp))

            // Senha
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError,
                errorMessage = if (passwordError) "At least 6 caracteres" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { confirmPasswordFocus.requestFocus() }),
                focusRequester = passwordFocus,
                onTabPressed = { confirmPasswordFocus.requestFocus() }
            )

            Spacer(Modifier.height(16.dp))

            // Confirmação de senha
            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm password",
                leadingIcon = Icons.Default.Lock,
                trailingIcon = {
                    Icon(
                        imageVector = if (confirmPassword == password && confirmPassword.isNotBlank()) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (confirmPassword == password && confirmPassword.isNotBlank())
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmError,
                errorMessage = if (confirmError) "Passwords don't match" else null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (formValid && !isPreview) {
                            viewModel.register(name, phone, email, password)
                        }
                    }
                ),
                focusRequester = confirmPasswordFocus,
                onTabPressed = null
            )

            Spacer(Modifier.height(24.dp))

            // Botão Registrar
            AuthButton(
                text = "Save",
                onClick = { viewModel.register(name, phone, email, password) },
                enabled = formValid
            )

            Spacer(Modifier.height(16.dp))

            // Links: Esquerda e Direita
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text("Forgot Password", color = MaterialTheme.colorScheme.secondary)
                }
                TextButton(onClick = onLoginClick) {
                    Text("Log In", color = MaterialTheme.colorScheme.secondary)
                }
            }

            // Estados
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
                    LaunchedEffect(Unit) {
                        onLoginClick() // Volta para Login
                    }
                    Text("Registered successfully!", color = MaterialTheme.colorScheme.primary)
                }
                else -> Unit
            }
        }
    }
}

// Validações
private fun isValidEmail(email: String): Boolean =
    email.isNotBlank() && Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)

private fun isValidPhoneWithCountry(phone: String): Boolean =
    phone.matches(Regex("""^(55)?[1-9]\d{9,10}$"""))

private fun isValidPassword(password: String): Boolean =
    password.length >= 6