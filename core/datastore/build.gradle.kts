plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.ichigo.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlinx-serialization")
}

android {
    namespace = "com.nei.ichigo.core.datastore"
    kotlin {
        compilerOptions {
            // https://youtrack.jetbrains.com/issue/KT-73255
            freeCompilerArgs.add("-Xannotation-default-target=first-only")
        }
    }
}

dependencies {

    implementation(projects.core.model)

    // datastore
    implementation(libs.androidx.datastore.preferences)

}