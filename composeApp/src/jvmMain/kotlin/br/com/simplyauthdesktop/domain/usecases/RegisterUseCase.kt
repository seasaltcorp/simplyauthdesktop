package br.com.simplyauthdesktop.domain.usecases

import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.entities.User
import br.com.simplyauthdesktop.domain.repositories.AuthRepository
import de.mkammerer.argon2.Argon2Factory
import java.util.UUID

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, phone: String, email: String, password: String): Result<User> {
        val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)
        val hashedPassword = argon2.hash(2, 65535, 1, password.toCharArray())
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            phone = phone,
            name = name,
            email = email,
            hashedPassword = hashedPassword,
            isActive = true,
            isVerified = false
        )
        return repository.register(user)
    }
}