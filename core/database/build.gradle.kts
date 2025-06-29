plugins {
    alias(libs.plugins.ichigo.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ichigo.hilt)
}

android {
    namespace = "com.nei.ichigo.core.database"
    defaultConfig {
        //noinspection WrongGradleMethod
        ksp {
            arg("room.schemaLocation", "$projectDir/core/database/schemas")
        }
    }
}

dependencies {

    implementation(projects.core.model)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}