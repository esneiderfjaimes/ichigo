import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "com.nei.ichigo.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.ichigo.android.application.get().pluginId
            implementationClass = "com.nei.ichigo.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.ichigo.android.library.get().pluginId
            implementationClass = "com.nei.ichigo.AndroidLibraryConventionPlugin"
        }
        register("androidLint") {
            id = libs.plugins.ichigo.android.lint.get().pluginId
            implementationClass = "com.nei.ichigo.AndroidLintConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.ichigo.hilt.get().pluginId
            implementationClass = "com.nei.ichigo.HiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.ichigo.jvm.library.get().pluginId
            implementationClass = "com.nei.ichigo.JvmLibraryConventionPlugin"
        }
    }
}
