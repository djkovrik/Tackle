plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("test-junit"))

                implementation(project(":shared:domain"))

                implementation(libs.ark.decompose.core)
                implementation(libs.ark.essenty)
                implementation(libs.ark.mvikotlin.core)
                implementation(libs.ark.mvikotlin.extensions)

                implementation(libs.lib.okio)
                implementation(libs.lib.ksoup)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
