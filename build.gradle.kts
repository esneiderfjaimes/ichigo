import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.version.catalog.update) apply true
    alias(libs.plugins.githooks)
    id("com.github.ben-manes.versions") version "0.52.0"
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    // Ignora versiones no estables (alfa, beta, rc, etc.)
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }

    // Puedes incluir/excluir por grupo o nombre
    checkForGradleUpdate = true
    outputFormatter = "json" // también: "plain", "xml", "html", "text"
    outputDir = "dependencyUpdates"
}

// Función utilitaria
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex() // e.g. 1.2.3, v1.2.3, 1.0.0-r
    return !stableKeyword && !regex.matches(version)
}

versionCatalogUpdate {
    sortByKey.set(false)
    keep {
        // keep versions without any library or plugin reference
        keepUnusedVersions.set(true)
    }
}

tasks.register("runVersionCatalogUpdate") {
    dependsOn("versionCatalogUpdate")
    group = "custom"
    description = "Runs the versionCatalogUpdate task"
}
