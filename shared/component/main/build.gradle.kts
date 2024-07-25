plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:domain"))
                implementation(project(":shared:database"))
                implementation(project(":shared:network"))
                implementation(project(":shared:settings"))
                implementation(project(":shared:utils"))

                implementation(project(":shared:component:tab:home"))
                implementation(project(":shared:component:tab:explore"))
                implementation(project(":shared:component:tab:editor"))
                implementation(project(":shared:component:tab:publications"))
                implementation(project(":shared:component:tab:notifications"))

                implementation(libs.ark.decompose.core)
                implementation(libs.ark.decompose.extensions)
                implementation(libs.ark.mvikotlin.core)
                implementation(libs.ark.mvikotlin.main)
                implementation(libs.ark.mvikotlin.extensions)
                implementation(libs.ark.essenty)
            }
        }
    }
}
