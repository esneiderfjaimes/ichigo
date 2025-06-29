package com.nei.ichigo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            libs.findLibrary("junit").ifPresent {
                "testImplementation"(it)
            }
            libs.findLibrary("androidx.junit").ifPresent {
                "androidTestImplementation"(it)
            }
            libs.findLibrary("androidx.espresso.core").ifPresent {
                "androidTestImplementation"(it)
            }
        }
    }
}