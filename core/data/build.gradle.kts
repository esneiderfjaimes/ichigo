plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.ichigo.hilt)
}

android {
    namespace = "com.nei.ichigo.core.data"
    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xannotation-default-target=param-property"
            )
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