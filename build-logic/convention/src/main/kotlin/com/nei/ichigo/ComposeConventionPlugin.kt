package com.nei.ichigo

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

@Suppress("unused")
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                optIn.addAll(
                    "androidx.compose.material3.ExperimentalMaterial3Api",
                    "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
                    "androidx.compose.animation.ExperimentalSharedTransitionApi",
                )
            }
        }

        when {
            project.plugins.hasPlugin("com.android.application") -> {
                configure<ApplicationExtension> {
                    buildFeatures {
                        compose = true
                    }
                }
            }

            project.plugins.hasPlugin("com.android.library") -> {
                configure<LibraryExtension> {
                    buildFeatures {
                        compose = true
                    }
                }
            }
        }

        dependencies {
            apply(plugin = libs.findPluginId("kotlin.compose"))

            libs.findLibrary("compose.bom").ifPresent {
                "implementation"(platform(it))
                "androidTestImplementation"(platform(it))
            }
            libs.findBundle("compose").ifPresent {
                "implementation"(it)
            }
            libs.findLibrary("compose.material3").ifPresent {
                "implementation"(it)
            }
            libs.findLibrary("compose.material.icons.extended").ifPresent {
                "implementation"(it)
            }
            libs.findLibrary("compose.ui.test.junit4").ifPresent {
                "androidTestImplementation"(it)
            }
            libs.findLibrary("compose.ui.tooling").ifPresent {
                "debugImplementation"(it)
            }
            libs.findLibrary("compose.ui.test.manifest").ifPresent {
                "debugImplementation"(it)
            }
        }
    }
}