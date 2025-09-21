package com.nei.ichigo.feature.encyclopedia.icons.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.encyclopedia.icons.IconUi
import com.nei.ichigo.feature.encyclopedia.icons.IconsScreen
import kotlinx.serialization.Serializable

@Serializable
data object IconsRoute

fun NavController.navigateToIcons(navOptions: NavOptions) = navigate(route = IconsRoute, navOptions)

context(sharedTransitionScope: SharedTransitionScope)
fun NavGraphBuilder.icons(
    onIconClick: (IconUi, String) -> Unit,
) {
    composable<IconsRoute> {
        IconsScreen(
            onIconClick = onIconClick,
        )
    }
}