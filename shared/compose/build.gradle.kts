plugins {
    alias(libs.plugins.compose)
    id("tackle.config.android")
    id("tackle.config.multiplatform")
    id("tackle.config.compose")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }

        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":shared:domain"))
            implementation(project(":shared:utils"))

            implementation(project(":shared:component:root"))
            implementation(project(":shared:component:auth"))
            implementation(project(":shared:component:main"))
            implementation(project(":shared:component:main:child:alternatetext"))
            implementation(project(":shared:component:main:child:viewmedia"))
            implementation(project(":shared:component:status"))
            implementation(project(":shared:component:statuslist"))

            implementation(project(":shared:component:editor"))
            implementation(project(":shared:component:editor:child:attachments"))
            implementation(project(":shared:component:editor:child:details"))
            implementation(project(":shared:component:editor:child:emojis"))
            implementation(project(":shared:component:editor:child:header"))
            implementation(project(":shared:component:editor:child:poll"))
            implementation(project(":shared:component:editor:child:warning"))

            implementation(project(":shared:component:tab:home"))
            implementation(project(":shared:component:tab:explore"))
            implementation(project(":shared:component:tab:publications"))
            implementation(project(":shared:component:tab:notifications"))
            implementation(project(":shared:component:tab:profile"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.ark.decompose.core)
            implementation(libs.ark.decompose.extensions)

            implementation(libs.lib.fileKit.core)
            implementation(libs.lib.fileKit.compose)
            implementation(libs.lib.blurhash)
            implementation(libs.lib.zoomImage)
            implementation(libs.lib.sketch.compose)
            implementation(libs.lib.sketch.compose.extensions)
            implementation(libs.lib.sketch.ktor3)
            implementation(libs.lib.sketch.gif)
            implementation(libs.lib.mediaPlayer)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")

    stabilityConfigurationFiles.addAll(
        project.layout.projectDirectory.file("compose_compiler_config.conf"),
    )
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
