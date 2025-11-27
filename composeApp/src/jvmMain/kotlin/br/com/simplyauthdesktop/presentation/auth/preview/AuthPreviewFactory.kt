package br.com.simplyauthdesktop.presentation.auth.preview

import br.com.simplyauthdesktop.data.FakeAuthRepository
import br.com.simplyauthdesktop.domain.usecases.LoginUseCase
import br.com.simplyauthdesktop.domain.usecases.RecoverPasswordUseCase
import br.com.simplyauthdesktop.domain.usecases.RegisterUseCase
import br.com.simplyauthdesktop.presentation.auth.AuthViewModel

object AuthPreviewFactory {
    private val fakeRepo = FakeAuthRepository()

    fun createViewModel(): AuthViewModel = AuthViewModel(
        loginUseCase = LoginUseCase(fakeRepo),
        registerUseCase = RegisterUseCase(fakeRepo),
        recoverPasswordUseCase = RecoverPasswordUseCase(fakeRepo)
    )
}