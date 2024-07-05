plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:domain"))
                implementation(project(":shared:utils"))

                implementation(project(":shared:component:tab:editor:child:attachments"))
                implementation(project(":shared:component:tab:editor:child:emojis"))
                implementation(project(":shared:component:tab:editor:child:header"))
                implementation(project(":shared:component:tab:editor:child:warning"))

                implementation(libs.ark.decompose.core)
                implementation(libs.ark.decompose.extensions)
                implementation(libs.ark.mvikotlin.core)
                implementation(libs.ark.mvikotlin.main)
                implementation(libs.ark.mvikotlin.extensions)
                implementation(libs.ark.essenty)

                implementation(libs.lib.fileKit.core)
            }
        }
    }
}
