package com.nei.ichigo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.nei.ichigo.R
import com.nei.ichigo.feature.encyclopedia.champion.navigation.champion
import com.nei.ichigo.feature.encyclopedia.champion.navigation.navigateToChampion
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.champions
import com.nei.ichigo.feature.encyclopedia.champions.navigation.navigateToChampions
import com.nei.ichigo.feature.encyclopedia.champions2pane.SUPPORT_PANE_CHAMPION
import com.nei.ichigo.feature.encyclopedia.champions2pane.championsListDetail
import com.nei.ichigo.feature.encyclopedia.icons.navigation.IconsRoute
import com.nei.ichigo.feature.encyclopedia.icons.navigation.icons
import com.nei.ichigo.feature.encyclopedia.icons.navigation.navigateToIcons
import com.nei.ichigo.feature.encyclopedia.items.navigation.ItemsRoute
import com.nei.ichigo.feature.encyclopedia.items.navigation.items
import com.nei.ichigo.feature.encyclopedia.items.navigation.navigateToItems
import com.nei.ichigo.feature.licenses.navigation.licencesScreen
import com.nei.ichigo.feature.licenses.navigation.navigateToLicences
import com.nei.ichigo.feature.settings.navigation.EncyclopediaSettingsRoute
import com.nei.ichigo.feature.settings.navigation.encyclopediaSettings
import com.nei.ichigo.feature.settings.navigation.navigateToEncyclopediaSettings

sealed class Screen(
    val route: String,
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val action: NavController.() -> Unit
) {
    data object Champions : Screen(
        route = ChampionsRoute.javaClass.name,
        title = R.string.champions,
        icon = Icons.Default.Circle,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToChampions(navOptions)
        }
    )

    data object ProfileIcons : Screen(
        route = IconsRoute.javaClass.name,
        title = R.string.icons,
        icon = Icons.Default.Circle,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToIcons(navOptions)
        }
    )


    data object Items : Screen(
        route = ItemsRoute.javaClass.name,
        title = R.string.items,
        icon = Icons.Default.Circle,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToItems(navOptions)
        }
    )

    data object Settings : Screen(
        route = EncyclopediaSettingsRoute.javaClass.name,
        title = R.string.settings,
        icon = Icons.Default.Settings,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToEncyclopediaSettings(navOptions)
        }
    )

    companion object {
        val allScreens = listOf(Champions, ProfileIcons, Items, Settings)
    }
}

private fun NavController.topLevelDestinationNavOptions() = navOptions {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }

    launchSingleTop = true
    restoreState = true
}

@Composable
fun IchigoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ChampionsRoute,
    ) {
        if (SUPPORT_PANE_CHAMPION) {
            championsListDetail()
        } else {
            champions(onChampionClick = { navController.navigateToChampion(it) })
            champion(onBackPress = { navController.popBackStack() })
        }
        icons()
        items()
        encyclopediaSettings(onLicenseClick = { navController.navigateToLicences() })
        licencesScreen(onBackPress = { navController.popBackStack() })
    }
}