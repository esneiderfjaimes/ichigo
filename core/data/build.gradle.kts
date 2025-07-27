plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.ichigo.hilt)
}

android {
    namespace = "com.nei.ichigo.core.data"
    kotlin {
        compilerOptions {
            // https://youtrack.jetbrains.com/issue/KT-73255
            freeCompilerArgs.add("-Xannotation-default-target=first-only")
        }
    }
}

dependencies {

    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(projects.core.database)

    implementation(libs.androidx.core.ktx)
}