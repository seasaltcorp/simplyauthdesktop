package br.com.simplyauthdesktop.data

import app.cash.sqldelight.db.SqlDriver
import br.com.simplyauthdesktop.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    // 1. Driver (run migration)
    single<SqlDriver> { DatabaseDriverFactory().create() }

    // 2. AppDatabase
    single { AppDatabase(get()) }

    // 3. Queries (optional, if needs to inject)
    single { get<AppDatabase>().appDatabaseQueries }
}