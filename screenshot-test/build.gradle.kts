plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.ichigo.compose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.nei.ichigo.screenshottest"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true // importante para Robolectric
            all {
                // mejora la precisión de la captura
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}

dependencies {

    testImplementation(projects.app)
    testImplementation(projects.core.designsystemy)
    testImplementation(projects.core.model)

    // Compose test lib — usa la versión de Compose que tengas en tu proyecto
    testImplementation("androidx.compose.ui:ui-test-junit4")

    api(libs.roborazzi)
    api(libs.roborazzi.accessibility.check)
    implementation(libs.robolectric)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}