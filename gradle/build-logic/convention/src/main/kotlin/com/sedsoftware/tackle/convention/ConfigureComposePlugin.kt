package com.sedsoftware.tackle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigureComposePlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("compose").get().get().pluginId)
            apply(libs.findPlugin("compose.compiler").get().get().pluginId)
        }
    }
}
