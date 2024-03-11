import com.sedsoftware.tackle.convention.libs
import com.sedsoftware.tackle.convention.preconfigure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class ConfigureMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKotlinMultiplatform()
    }
}

internal fun Project.configureKotlinMultiplatform() {
    preconfigure<KotlinMultiplatformExtension> {
        jvmToolchain(17)

        androidTarget()
        iosX64()
        iosArm64()
        iosSimulatorArm64()
        jvm("desktop")

        applyDefaultHierarchyTemplate()

        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.configureEach {
                linkerOpts("-lsqlite3")
                freeCompilerArgs += "-Xdisable-phases=RemoveRedundantCallsToStaticInitializersPhase"
            }
        }

        sourceSets.apply {
            commonMain {
                dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                    implementation(libs.findLibrary("kotlinx.datetime").get())
                    implementation(libs.findLibrary("kotlinx.serialization.json").get())
                    implementation(libs.findLibrary("napier").get())
                }
            }

            androidMain {
                dependencies {
                    implementation(libs.findLibrary("androidx.appcompat").get())
                    implementation(libs.findLibrary("androidx.core.ktx").get())
                    implementation(libs.findLibrary("androidx.material").get())
                }
            }

            commonTest.dependencies {
                implementation(kotlin("test"))
                implementation(libs.findLibrary("kotlinx.datetime").get())
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
                implementation(libs.findLibrary("test.kotlin").get())
                implementation(libs.findLibrary("test.junit").get())
                implementation(libs.findLibrary("test.kotlin.junit").get())
            }
        }
    }
}
