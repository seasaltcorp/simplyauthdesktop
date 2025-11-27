package br.com.simplyauthdesktop.data

import br.com.simplyauthdesktop.domain.entities.User
import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

class FakeAuthRepository: AuthRepository {
    private var currentUser: User? = null

    override suspend fun register(user: User): Result<User> {
        val newUser = user.copy(id = UUID.randomUUID())
        currentUser = newUser
        return Result.Success(newUser)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        // Pretender successfull login
        val user = User(
            id = UUID.randomUUID(),
            phone = "5548991995543",
            email = email,
            hashedPassword = "fake_hash",
            name = "Test User",
            isActive = true,
            isVerified = true
        )
        currentUser = user
        return Result.Success(user)
    }

    override suspend fun recoverPassword(identifier: String): Result<String> {
        return if (identifier.contains("@") || identifier.length >= 10) {
            Result.Success("Sent token to $identifier")
        } else {
            Result.Error("Invalid identifier")
        }
    }

    override fun getCurrentUser(): Flow<User?> = flowOf(currentUser)
}