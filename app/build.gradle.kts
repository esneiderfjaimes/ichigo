plugins {
    alias(libs.plugins.ichigo.android.application)
    alias(libs.plugins.ichigo.compose)
    alias(libs.plugins.ichigo.hilt)
    id("kotlinx-serialization")
    id("com.mikepenz.aboutlibraries.plugin")
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
    kotlin {
        compilerOptions {
            optIn.addAll(
                "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
            )
            freeCompilerArgs.addAll(
                "-Xcontext-parameters",
                "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // project
    implementation(projects.core.designsystemy)
    implementation(projects.core.data)
    implementation(projects.core.model)

    // core
    implementation(libs.androidx.core.ktx)

    // compose
    implementation(libs.androidx.activity.compose)
    // > adaptive
    implementation(libs.bundles.compose.adaptive)
    implementation(libs.androidx.core.splashscreen)

    // lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // hilt
    implementation(libs.hilt.navigation.compose)

    // about libraries
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.m3)
}