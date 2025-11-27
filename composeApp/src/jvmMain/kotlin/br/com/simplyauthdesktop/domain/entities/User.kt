package br.com.simplyauthdesktop.domain.entities

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val phone: String,
    val hashedPassword: String,
    val isActive: Boolean = true,
    val isVerified: Boolean = false
)
