plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ichigo.hilt)
    id("kotlinx-serialization")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nei.ichigo.core.datastore"
}

dependencies {

    implementation(projects.core.model)

    implementation (libs.androidx.datastore.preferences)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}