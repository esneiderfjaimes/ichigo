plugins {
    alias(libs.plugins.ichigo.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ichigo.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.nei.ichigo"

    defaultConfig {
        applicationId = "com.nei.ichigo"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
            "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
            "-Xcontext-receivers",
            // TODO: migrate in Kotlin 2.2 "-Xcontext-parameters"
        )
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // project
    implementation(projects.core.model)
    implementation(projects.core.data)

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    // > material
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    // > adaptive
    implementation(libs.bundles.compose.adaptive)

    // lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // hilt
    implementation(libs.hilt.navigation.compose)

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