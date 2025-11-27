package br.com.simplyauthdesktop.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import br.com.simplyauthdesktop.AppDatabase
import java.io.File

class DatabaseDriverFactory() {
    fun create(): SqlDriver {
        val dbPath = File(System.getProperty("user.home"), ".simplyauthdesktop/app.db")
        dbPath.parentFile?.mkdirs()

        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbPath.absolutePath}")

        // Migrations
        val oldVersion = getCurrentVersion(driver)
        AppDatabase.Schema.create(driver)
        AppDatabase.Schema.migrate(driver, oldVersion, AppDatabase.Schema.version)
        logMigration(oldVersion, AppDatabase.Schema.version)

        return driver
    }

    private fun getCurrentVersion(driver: SqlDriver): Long {
        return try {
            driver.executeQuery(
                null,
                "PRAGMA user_version;",
                mapper = { cursor ->
                    app.cash.sqldelight.db.QueryResult.Value(
                        if (cursor.next().value) cursor.getLong(0) ?: 0L else 0L
                    )
                },
                0
            ).value
        } catch (e: Exception) {
            println("Erro ao testar o banco de dados:  ${e.message}")
            0L // Database corrupt
        }
    }

    private fun logMigration(oldVersion: Long, newVersion: Long) {
        println("")
        println("<=======================================>")
        println(" Staring database migrations...")
        println(" Old version: $oldVersion → New: $newVersion")
        println(" Migrations conluded!")
        println(" Current user version: $newVersion")
        println("<=======================================>")
        println("")
    }
}