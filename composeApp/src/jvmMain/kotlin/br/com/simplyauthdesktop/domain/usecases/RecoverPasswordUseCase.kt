package br.com.simplyauthdesktop.domain.usecases

import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.repositories.AuthRepository

class RecoverPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(identifier: String): Result<String> {
        return repository.recoverPassword(identifier)
    }
}