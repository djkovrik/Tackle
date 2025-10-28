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

                implementation(project(":shared:component:auth"))
                implementation(project(":shared:component:main"))
                implementation(project(":shared:component:main:child:alternatetext"))
                implementation(project(":shared:component:main:child:viewmedia"))
                implementation(project(":shared:component:editor"))
                implementation(project(":shared:component:status"))
                implementation(project(":shared:component:statuslist"))

                implementation(libs.ark.decompose.core)
                implementation(libs.ark.decompose.extensions)
                implementation(libs.ark.mvikotlin.core)
                implementation(libs.ark.mvikotlin.main)
                implementation(libs.ark.mvikotlin.extensions)
                implementation(libs.ark.essenty)

                implementation(libs.lib.settings.core)

                implementation(libs.oidc.appsupport)
            }
        }
    }
}
