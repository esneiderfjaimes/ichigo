package com.nei.ichigo.screenshottest

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziOptions.CompareOptions
import com.github.takahirom.roborazzi.RoborazziOptions.RecordOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.nei.ichigo.IchigoApplication
import com.nei.ichigo.common.UiState
import com.nei.ichigo.core.designsystem.theme.IchigoThemePreview
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE) // necesario para Robolectric native graphics
@Config(application = IchigoApplication::class)
class HomeRoborazziTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    // RoborazziRule es opcional, sirve para opciones centralizadas
    /*    @get:Rule
        val roborazziRule = RoborazziRule(
            composeRule = composeRule,
            captureRoot = composeRule.onRoot()
        )*/
    val DefaultRoborazziOptions =
        RoborazziOptions(
            // Pixel-perfect matching
            compareOptions = CompareOptions(changeThreshold = 0f),
            // Reduce the size of the PNGs
            recordOptions = RecordOptions(resizeScale = 0.5),
        )

    @Test
    fun captureButton() {
        composeRule.setContent {
            IchigoThemePreview {
                ChampionsScreen(
                    state = UiState.Loading
                )
            }
        }

        // Espera que Compose esté idle
        composeRule.waitForIdle()

        val screenshotName = "button"
        val deviceName = "device"

        composeRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/${screenshotName}_$deviceName.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }
}