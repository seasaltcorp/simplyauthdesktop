package br.com.simplyauthdesktop.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.entities.User
import br.com.simplyauthdesktop.domain.usecases.LoginUseCase
import br.com.simplyauthdesktop.domain.usecases.RecoverPasswordUseCase
import br.com.simplyauthdesktop.domain.usecases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val recoverPasswordUseCase: RecoverPasswordUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            when (val r = loginUseCase(email, password)) {
                is Result.Success -> _uiState.value = AuthState.Success(r.data)
                is Result.Error -> _uiState.value = AuthState.Error(r.message)
            }
        }
    }

    fun register(name: String, phone: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            when (val r = registerUseCase(name, phone, email, password)) {
                is Result.Success -> _uiState.value = AuthState.Success(r.data)
                is Result.Error -> _uiState.value = AuthState.Error(r.message)
            }
        }
    }

    fun recoverPassword(identifier: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            when (val r = recoverPasswordUseCase(identifier)) {
                is Result.Success -> _uiState.value = AuthState.RecoverySuccess(r.data)
                is Result.Error -> _uiState.value = AuthState.Error(r.message)
            }
        }
    }

    /** Clean state – called on out of screen */
    fun reset() {
        _uiState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class RecoverySuccess(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}