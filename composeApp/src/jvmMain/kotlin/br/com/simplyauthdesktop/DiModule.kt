package br.com.simplyauthdesktop

import br.com.simplyauthdesktop.data.SqlAuthRepository
import br.com.simplyauthdesktop.data.databaseModule
import br.com.simplyauthdesktop.domain.repositories.AuthRepository
import br.com.simplyauthdesktop.domain.usecases.LoginUseCase
import br.com.simplyauthdesktop.domain.usecases.RecoverPasswordUseCase
import br.com.simplyauthdesktop.domain.usecases.RegisterUseCase
import br.com.simplyauthdesktop.presentation.auth.AuthViewModel
import org.koin.dsl.module

val appModule = module {
    includes(databaseModule)

    // Repositories
    single<AuthRepository> { SqlAuthRepository(get()) }

    // Use Cases
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { RecoverPasswordUseCase(get()) }
    factory { AuthViewModel(get(), get(), get()) }
}