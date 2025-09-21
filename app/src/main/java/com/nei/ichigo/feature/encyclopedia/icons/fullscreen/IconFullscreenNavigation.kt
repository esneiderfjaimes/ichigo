package com.nei.ichigo.feature.encyclopedia.icons.fullscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nei.ichigo.common.utils.AnimatedContentScopeProvider
import com.nei.ichigo.feature.encyclopedia.icons.IconUi
import kotlinx.serialization.Serializable

@Serializable
data class IconScreenshotRoute(val id: String, val image: String, val version: String)

fun NavController.navigateToIconFullscreen(
    icon: IconUi,
    version: String,
) = navigate(route = IconScreenshotRoute(id = icon.id, image = icon.image, version = version))

fun NavGraphBuilder.iconFullscreen(onBackPress: () -> Unit) {
    composable<IconScreenshotRoute> { entry ->
        val route = entry.toRoute<IconScreenshotRoute>()
        val icon = IconUi(id = route.id, image = route.image)
        AnimatedContentScopeProvider {
            IconFullscreenScreen(
                icon = icon,
                version = route.version,
                onBackPress = onBackPress
            )
        }
    }
}