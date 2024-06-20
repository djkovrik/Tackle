plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:network"))
                implementation(project(":shared:settings"))
                implementation(project(":shared:utils"))
                
                implementation(project(":shared:component:tab:home"))
                implementation(project(":shared:component:tab:browse"))
                implementation(project(":shared:component:tab:editor"))
                implementation(project(":shared:component:tab:feeds"))
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
