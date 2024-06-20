plugins {
    alias(libs.plugins.compose)
    id("tackle.config.android")
    id("tackle.config.multiplatform")
    id("tackle.config.compose")
}

kotlin {
    task("testClasses")

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }

        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":shared:component:root"))
            implementation(project(":shared:component:auth"))
            implementation(project(":shared:component:main"))

            implementation(project(":shared:component:tab:home"))
            implementation(project(":shared:component:tab:explore"))
            implementation(project(":shared:component:tab:editor"))
            implementation(project(":shared:component:tab:publications"))
            implementation(project(":shared:component:tab:notifications"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.ark.decompose.core)
            implementation(libs.ark.decompose.extensions)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}
