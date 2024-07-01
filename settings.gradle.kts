@file:Suppress("UnstableApiUsage")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Tackle"
include(
    ":composeApp",
    ":shared:compose",
    ":shared:component:root",
    ":shared:component:auth",
    ":shared:component:main",
    ":shared:component:tab:home",
    ":shared:component:tab:explore",
    ":shared:component:tab:editor",
    ":shared:component:tab:editor:subcomponent:attachments",
    ":shared:component:tab:editor:subcomponent:emojis",
    ":shared:component:tab:editor:subcomponent:header",
    ":shared:component:tab:editor:subcomponent:poll",
    ":shared:component:tab:publications",
    ":shared:component:tab:notifications",
    ":shared:domain",
    ":shared:database",
    ":shared:settings",
    ":shared:network",
    ":shared:utils",
)
includeBuild("gradle/build-logic")
