plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nei.ichigo.core.data"
}

dependencies {

    implementation(projects.core.model)

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}