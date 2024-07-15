plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:domain"))

                implementation(libs.lib.settings.core)
                implementation(libs.lib.settings.test)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.preferences)
            }
        }
    }
}
