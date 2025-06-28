plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ichigo.hilt)
}

android {
    namespace = "com.nei.ichigo.core.designsystem"

    kotlin {
        compilerOptions {
            optIn.addAll(
                "androidx.compose.material3.ExperimentalMaterial3Api",
                "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
                "androidx.compose.animation.ExperimentalSharedTransitionApi",
                "coil3.annotation.ExperimentalCoilApi"
            )
            freeCompilerArgs.addAll(
                "-Xcontext-parameters"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // core
    implementation(libs.androidx.core.ktx)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    // > material
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    // > adaptive
    implementation(libs.bundles.compose.adaptive)

    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}