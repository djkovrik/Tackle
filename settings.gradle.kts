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
    ":shared:component:editor",
    ":shared:component:editor:child:attachments",
    ":shared:component:editor:child:details",
    ":shared:component:editor:child:emojis",
    ":shared:component:editor:child:header",
    ":shared:component:editor:child:poll",
    ":shared:component:editor:child:warning",
    ":shared:component:status",
    ":shared:component:tab:home",
    ":shared:component:tab:explore",
    ":shared:component:tab:publications",
    ":shared:component:tab:notifications",
    ":shared:component:tab:profile",
    ":shared:domain",
    ":shared:database",
    ":shared:settings",
    ":shared:network",
    ":shared:utils",
)
includeBuild("gradle/build-logic")
