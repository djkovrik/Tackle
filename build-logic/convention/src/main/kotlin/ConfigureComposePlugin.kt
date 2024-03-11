import com.sedsoftware.tackle.convention.preconfigure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension

class ConfigureComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureCompose()
    }
}

internal fun Project.configureCompose() {
    preconfigure<ComposeExtension> {
        kotlinCompilerPluginArgs.addAll(
            // Enable 'strong skipping'
            "experimentalStrongSkipping=true",
        )
    }
}
