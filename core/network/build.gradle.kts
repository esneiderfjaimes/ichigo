plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ichigo.hilt)
    id("kotlinx-serialization")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nei.ichigo.core.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(projects.core.model)

    implementation(libs.squareup.logging.interceptor)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.gson)
    implementation(libs.kotlin.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}