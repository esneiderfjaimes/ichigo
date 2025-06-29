plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.ichigo.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlinx-serialization")
}

android {
    namespace = "com.nei.ichigo.core.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(projects.core.model)

    // squareup
    implementation(libs.squareup.logging.interceptor)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.gson)

    implementation(libs.kotlin.serialization.json)
}