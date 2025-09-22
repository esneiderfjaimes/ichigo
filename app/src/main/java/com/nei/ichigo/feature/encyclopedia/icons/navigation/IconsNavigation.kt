package com.nei.ichigo.feature.encyclopedia.icons.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.common.utils.AnimatedVisibilityScopeProvider
import com.nei.ichigo.feature.encyclopedia.icons.IconUi
import com.nei.ichigo.feature.encyclopedia.icons.IconsScreen
import kotlinx.serialization.Serializable

@Serializable
data object IconsRoute

fun NavController.navigateToIcons(navOptions: NavOptions) = navigate(route = IconsRoute, navOptions)

fun NavGraphBuilder.icons(
    onIconClick: (IconUi, String) -> Unit,
) {
    composable<IconsRoute> {
        AnimatedVisibilityScopeProvider {
            IconsScreen(
                onIconClick = onIconClick,
            )
        }
    }
}