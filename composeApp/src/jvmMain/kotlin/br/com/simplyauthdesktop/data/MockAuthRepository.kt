package br.com.simplyauthdesktop.data

import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.entities.User
import br.com.simplyauthdesktop.domain.repositories.AuthRepository
import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class MockAuthRepository: AuthRepository {
    private val users = mutableMapOf<String, User>() // Chave: Phone
    private val currentUser = MutableStateFlow<User?>(null)
    private val argon2: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    override suspend fun register(user: User): Result<User> {
        if (users.containsKey(user.email) || users.containsKey(user.phone)) {
            return Result.Error("User already exists")
        }

        users[user.email] = user
        currentUser.value = user
        return Result.Success(user)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        val user = users[email] ?: return Result.Error("User not found")

        val verifyPassword = argon2.verify(user.hashedPassword, password.toCharArray())

        if (!verifyPassword) {
            return Result.Error("Wrong password")
        }

        currentUser.value = user
        return Result.Success(user)
    }

    override suspend fun recoverPassword(identifier: String): Result<String> {
        val user = users.values.find { it.phone == identifier || it.email == identifier }
            ?: return  Result.Error("User not found")

        // Mock: "Envio" de token para console
        val token = UUID.randomUUID().toString()
        println("Sent recovery token to ${user.email}: $token")  // Simula email
        return Result.Success("Sent token to registered email")
    }

    override fun getCurrentUser(): Flow<User?> = currentUser
}