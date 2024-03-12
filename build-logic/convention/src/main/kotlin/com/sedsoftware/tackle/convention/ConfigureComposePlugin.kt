package com.sedsoftware.tackle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension

class ConfigureComposePlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("compose").get().get().pluginId)
        }
        configureCompose()
    }
}

internal fun Project.configureCompose() {
    preconfigure<ComposeExtension> {
        kotlinCompilerPluginArgs.addAll(
            // Enable 'strong skipping'
            // https://medium.com/androiddevelopers/jetpack-compose-strong-skipping-mode-explained-cbdb2aa4b900
            "experimentalStrongSkipping=true",
        )
    }
}
