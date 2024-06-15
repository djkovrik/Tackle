plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ark.decompose.core)
                implementation(libs.ark.essenty)
                implementation(libs.ark.mvikotlin.core)
                implementation(libs.ark.mvikotlin.extensions)
            }
        }
    }
}
