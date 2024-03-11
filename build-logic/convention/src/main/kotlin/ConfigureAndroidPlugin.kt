import com.android.build.api.dsl.LibraryExtension
import com.sedsoftware.tackle.convention.libs
import com.sedsoftware.tackle.convention.preconfigure
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigureAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureKotlinAndroid()
    }
}

internal fun Project.configureKotlinAndroid() {
    preconfigure<LibraryExtension> {
        val moduleName = path.split(":").drop(2).joinToString(".")
        namespace = if (moduleName.isNotEmpty()) "com.sedsoftware.shared.$moduleName" else "com.sedsoftware.shared"
        println("Namespace for module $moduleName: $namespace")
        compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}
