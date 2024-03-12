plugins {
    `kotlin-dsl`
}

group = "com.sedsoftware.tackle.convention"

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("configureAndroid") {
            id = "tackle.config.android"
            implementationClass = "com.sedsoftware.tackle.convention.ConfigureAndroidPlugin"
        }
        register("configureCompose") {
            id = "tackle.config.compose"
            implementationClass = "com.sedsoftware.tackle.convention.ConfigureComposePlugin"
        }
        register("configureMultiplatform") {
            id = "tackle.config.multiplatform"
            implementationClass = "com.sedsoftware.tackle.convention.ConfigureMultiplatformPlugin"
        }
    }
}
