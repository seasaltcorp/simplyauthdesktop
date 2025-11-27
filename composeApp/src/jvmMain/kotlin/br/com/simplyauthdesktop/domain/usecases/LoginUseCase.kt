package br.com.simplyauthdesktop.domain.usecases

import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.entities.User
import br.com.simplyauthdesktop.domain.repositories.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}