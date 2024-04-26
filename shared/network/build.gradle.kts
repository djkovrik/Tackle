plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.client.negotiation)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.ktor.client.desktop)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
    }
}
