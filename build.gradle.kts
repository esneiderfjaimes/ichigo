// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
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