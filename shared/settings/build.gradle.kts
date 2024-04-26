plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain {
            dependencies {
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
