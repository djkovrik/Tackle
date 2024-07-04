plugins {
    id("tackle.config.android")
    id("tackle.config.multiplatform")
    alias(libs.plugins.sqlDelight)
}

sqldelight {
    databases {
        create("TackleAppDatabase") {
            packageName.set("com.sedsoftware.tackle.database")
        }
    }
}

kotlin {
    task("testClasses")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:domain"))

                implementation(libs.lib.sqlDelight.coroutines)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.lib.sqlDelight.driver.android)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.lib.sqlDelight.driver.sqlite)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.lib.sqlDelight.driver.native)
            }
        }
    }
}
