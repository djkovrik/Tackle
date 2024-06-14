plugins {
    id("com.louiscad.complete-kotlin") version "1.1.0"

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.kotlinx.parcelize) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.sqlDelight) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

detekt {
    allRules = false
    parallel = true
    buildUponDefaultConfig = true
    baseline = file("$projectDir/detekt/baseline.xml")
    config.setFrom(file("$projectDir/detekt/base-config.yml"))
    source.setFrom(files("$projectDir/shared/"))
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("$projectDir/detekt/reports/detekt.html"))
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "1.8"
}

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "com.sedsoftware.tackle.*.integration.*Preview",
                )
            }
            includes {
                classes(
                    "com.sedsoftware.tackle.*.domain.*",
                    "com.sedsoftware.tackle.*.extension.*",
                    "com.sedsoftware.tackle.*.integration.*Default",
                    "com.sedsoftware.tackle.*.store.*",
                )
            }
        }
    }
}

dependencies {
    kover(project(":shared:component:auth"))
}
