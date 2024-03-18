plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ark.decompose.core)
                implementation(libs.ark.essenty)
            }
        }
    }
}
