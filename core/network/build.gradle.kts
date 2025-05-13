plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nei.ichigo.core.network"
}

dependencies {

    implementation(projects.core.model)

    implementation(libs.coil.kt)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}