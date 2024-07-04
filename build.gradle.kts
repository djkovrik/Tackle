import java.nio.file.Files
import javax.xml.parsers.DocumentBuilderFactory

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
    alias(libs.plugins.mokkery) apply false
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
    exclude("**/build/**")
}

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
    exclude("**/build/**")
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
    rootProject.subprojects {
        if (!this.path.contains("root") && !this.path.contains("compose") && file("build.gradle.kts").exists()) {
            kover(this)
        }
    }
}

// Src: https://bitspittle.dev/blog/2022/kover-badge
tasks.register("printLineCoverage") {
    group = "verification"
    dependsOn("koverXmlReport")
    doLast {
        val report = file("${rootProject.projectDir}/build/reports/kover/report.xml")

        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(report)
        val rootNode = doc.firstChild
        var childNode = rootNode.firstChild

        var coveragePercent = 0.0

        while (childNode != null) {
            if (childNode.nodeName == "counter") {
                val typeAttr = childNode.attributes.getNamedItem("type")
                if (typeAttr.textContent == "LINE") {
                    val missedAttr = childNode.attributes.getNamedItem("missed")
                    val coveredAttr = childNode.attributes.getNamedItem("covered")

                    val missed = missedAttr.textContent.toLong()
                    val covered = coveredAttr.textContent.toLong()

                    coveragePercent = (covered * 100.0) / (missed + covered)

                    break
                }
            }
            childNode = childNode.nextSibling
        }

        println("%.1f".format(coveragePercent))
    }
}
