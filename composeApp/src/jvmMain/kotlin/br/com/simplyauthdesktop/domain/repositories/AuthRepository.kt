package br.com.simplyauthdesktop.domain.repositories

import br.com.simplyauthdesktop.domain.common.Result
import br.com.simplyauthdesktop.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(user: User): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun recoverPassword(identifier: String): Result<String>
    fun getCurrentUser(): Flow<User?>
}