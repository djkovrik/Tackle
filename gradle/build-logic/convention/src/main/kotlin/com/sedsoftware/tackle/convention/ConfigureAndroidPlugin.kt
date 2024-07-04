package com.sedsoftware.tackle.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigureAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android.library").get().get().pluginId)
            }
            configureKotlinAndroid()
        }
}

internal fun Project.configureKotlinAndroid() {
    preconfigure<LibraryExtension> {
        val moduleName = path.split(":").drop(2).joinToString(".")
        namespace = if (moduleName.isNotEmpty()) "com.sedsoftware.tackle.$moduleName" else "com.sedsoftware.shared"
        compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
            addManifestPlaceholders(
                mapOf("oidcRedirectScheme" to "tackle")
            )
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}
