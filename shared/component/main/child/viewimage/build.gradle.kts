plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:domain"))

                implementation(libs.ark.decompose.core)
                implementation(libs.ark.decompose.extensions)
            }
        }
    }
}

