import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Novas dependências
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.argon2)
            implementation(libs.sqlite.driver)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        sqldelight {
            databases {
                create("AppDatabase") {
                    packageName.set("br.com.simplyauthdesktop")
                    srcDirs.setFrom(file("src/jvmMain/sqldelight"))

                    val customOutputDir = File(
                        System.getProperty("user.home"),
                        ".simplyauthdesktop").apply {
                        mkdirs()
                    }

                    schemaOutputDirectory.set(customOutputDir)
                    verifyMigrations.set(false)
                    treatNullAsUnknownForEquality.set(true)
                }
            }
        }
    }
}


compose.desktop {
    application {
        mainClass = "br.com.simplyauthdesktop.MainKt"
        jvmArgs += "--enable-native-access=ALL-UNNAMED"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "br.com.simplyauthdesktop"
            packageVersion = "1.0.0"
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "br.com.simplyauthdesktop.resources"
    generateResClass = always
}
