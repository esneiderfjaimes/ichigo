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
            freeCompilerArgs.addAll(
                "-Xannotation-default-target=param-property"
            )
        }
    }
}

dependencies {

    implementation(projects.core.model)

    // datastore
    implementation(libs.androidx.datastore.preferences)

}