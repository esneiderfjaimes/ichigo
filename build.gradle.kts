import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.github.esneiderfjaimes.modgraph.GenerateModGraphTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false

    // extras
    alias(libs.plugins.githooks)
    alias(libs.plugins.modgraph)
    alias(libs.plugins.littlerobots.version.catalog.update) apply true
    alias(libs.plugins.benmanes.versions) apply true
    alias(libs.plugins.aboutlibraries) apply true
}

tasks.named<GenerateModGraphTask>("generateModuleDependencyGraph") {
    // configure generation of module dependency graph
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }

    checkForGradleUpdate = true
    outputFormatter = "json" // "plain", "xml", "html", "text"
    // outputDir = "dependencyUpdates"
}

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
