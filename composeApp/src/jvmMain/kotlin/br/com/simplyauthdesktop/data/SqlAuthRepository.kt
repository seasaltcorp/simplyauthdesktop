package br.com.simplyauthdesktop.data

import br.com.simplyauthdesktop.domain.common.Result

import br.com.simplyauthdesktop.domain.entities.User
import br.com.simplyauthdesktop.domain.repositories.AuthRepository
import databases.AppDatabaseQueries
import de.mkammerer.argon2.Argon2Factory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class SqlAuthRepository(
    private val queries: AppDatabaseQueries
): AuthRepository {
    private val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    override suspend fun register(user: User): Result<User> = try {
        val count = queries.isFirstUser().executeAsOneOrNull()
        println("Total users: $count")

        queries.insertUser(
            id = user.id.toString(),
            name = user.name,
            email = user.email,
            phone = user.phone,
            password_hash = user.hashedPassword,
        )

        Result.Success(user)
    } catch (e: Exception) {
        Result.Error(
            if (e.message?.contains("UNIQUE") == true) {
                "E-mail or phone already registered"
            }
            else {
                println(e.message)
                "Error registering user"
            }
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        val row = queries.selectByEmail(email).executeAsOneOrNull()
            ?: return Result.Error("User or password invalid")

        return if (argon2.verify(row.password_hash, password.toCharArray())) {
            Result.Success(userFromRow(row))
        } else {
            Result.Error("User or password invalid")
        }
    }

    override suspend fun recoverPassword(identifier: String): Result<String> {
        val dbUser = queries.selectByPhone(identifier).executeAsOneOrNull()
            ?: queries.selectByEmail(identifier).executeAsOneOrNull()
            ?: return Result.Error("User not found")

        return Result.Success("Token sent to ${dbUser.email}")
    }

    override fun getCurrentUser(): Flow<User?> = flow { emit(null) }

    private fun userFromRow(row: databases.User): User = User(
        id = UUID.fromString(row.id),
        phone = row.phone,
        email = row.email,
        hashedPassword = row.password_hash,
        name = row.name,
        isActive = row.is_active == 1L,
        isVerified = row.is_verified == 1L
    )
}