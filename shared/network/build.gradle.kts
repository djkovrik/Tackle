plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:domain"))
                implementation(project(":shared:utils"))

                implementation(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.client.negotiation)

                implementation(libs.oidc.appsupport)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.lib.okio)
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
