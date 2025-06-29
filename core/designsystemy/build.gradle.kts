plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.ichigo.compose)
    alias(libs.plugins.ichigo.hilt)
}

android {
    namespace = "com.nei.ichigo.core.designsystem"

    // TODO             "-opt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
    kotlin {
        compilerOptions {
            optIn.addAll(
                "coil3.annotation.ExperimentalCoilApi"
            )
            freeCompilerArgs.addAll(
                "-Xcontext-parameters"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // core
    implementation(libs.androidx.core.ktx)

    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}