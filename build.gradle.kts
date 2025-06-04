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
}

versionCatalogUpdate {
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

tasks.register<Copy>("installGitHooks") {
    description = "Installs Git hooks from the hooks/ directory"
    group = "git"

    from("hooks") {
        include("*")
        filePermissions {
            unix("rwxr-xr-x")
        }
    }
    into(".git/hooks")
}

tasks.matching { it.name == "build" }.configureEach {
    dependsOn("installGitHooks")
}