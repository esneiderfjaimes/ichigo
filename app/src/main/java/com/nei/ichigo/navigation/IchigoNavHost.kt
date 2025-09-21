package com.nei.ichigo.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.icon.Champion
import com.nei.ichigo.core.designsystem.icon.IchigoIcons
import com.nei.ichigo.core.designsystem.icon.Item
import com.nei.ichigo.core.designsystem.icon.Rune
import com.nei.ichigo.core.designsystem.icon.Spell
import com.nei.ichigo.feature.encyclopedia.champion.navigation.champion
import com.nei.ichigo.feature.encyclopedia.champion.navigation.navigateToChampion
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.champions
import com.nei.ichigo.feature.encyclopedia.champions.navigation.navigateToChampions
import com.nei.ichigo.feature.encyclopedia.champions2pane.SUPPORT_PANE_CHAMPION
import com.nei.ichigo.feature.encyclopedia.champions2pane.championsListDetail
import com.nei.ichigo.feature.encyclopedia.icons.fullscreen.iconFullscreen
import com.nei.ichigo.feature.encyclopedia.icons.fullscreen.navigateToIconFullscreen
import com.nei.ichigo.feature.encyclopedia.icons.navigation.IconsRoute
import com.nei.ichigo.feature.encyclopedia.icons.navigation.icons
import com.nei.ichigo.feature.encyclopedia.icons.navigation.navigateToIcons
import com.nei.ichigo.feature.encyclopedia.items.navigation.ItemsRoute
import com.nei.ichigo.feature.encyclopedia.items.navigation.items
import com.nei.ichigo.feature.encyclopedia.items.navigation.navigateToItems
import com.nei.ichigo.feature.encyclopedia.runes.navigation.RunesRoute
import com.nei.ichigo.feature.encyclopedia.runes.navigation.navigateToRunes
import com.nei.ichigo.feature.encyclopedia.runes.navigation.runes
import com.nei.ichigo.feature.encyclopedia.spells.navigation.SpellsRoute
import com.nei.ichigo.feature.encyclopedia.spells.navigation.navigateToSpells
import com.nei.ichigo.feature.encyclopedia.spells.navigation.spells
import com.nei.ichigo.feature.licenses.navigation.licencesScreen
import com.nei.ichigo.feature.licenses.navigation.navigateToLicences
import com.nei.ichigo.feature.settings.navigation.EncyclopediaSettingsRoute
import com.nei.ichigo.feature.settings.navigation.encyclopediaSettings
import com.nei.ichigo.feature.settings.navigation.navigateToEncyclopediaSettings

enum class Screen(
    val route: String,
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val action: NavController.() -> Unit
) {
    Champions(
        route = ChampionsRoute.javaClass.name,
        title = R.string.champions,
        icon = IchigoIcons.Champion,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToChampions(navOptions)
        }
    ),
    ProfileIcons(
        route = IconsRoute.javaClass.name,
        title = R.string.icons,
        icon = IchigoIcons.ProfileIcons,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToIcons(navOptions)
        }
    ),
    Items(
        route = ItemsRoute.javaClass.name,
        title = R.string.items,
        icon = IchigoIcons.Item,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToItems(navOptions)
        }
    ),
    Spells(
        route = SpellsRoute.javaClass.name,
        title = R.string.spells,
        icon = IchigoIcons.Spell,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToSpells(navOptions)
        }
    ),
    Runes(
        route = RunesRoute.javaClass.name,
        title = R.string.runes,
        icon = IchigoIcons.Rune,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToRunes(navOptions)
        }
    ),
    Settings(
        route = EncyclopediaSettingsRoute.javaClass.name,
        title = R.string.settings,
        icon = IchigoIcons.Settings,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToEncyclopediaSettings(navOptions)
        }
    );

    companion object {
        val allScreens = entries.toList()
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
fun IchigoNavHost(
    navController: NavHostController,
    lastNavigationRoute: String?,
) {
    SharedTransitionScope { sharedTransitionModifier ->
        NavHost(
            modifier = sharedTransitionModifier,
            navController = navController,
            startDestination = resolveStartDestination(lastNavigationRoute),
        ) {
            if (SUPPORT_PANE_CHAMPION) {
                championsListDetail()
            } else {
                champions(onChampionClick = { navController.navigateToChampion(it) })
                champion(onBackPress = { navController.popBackStack() })
            }
            icons(onIconClick = { icon, version ->
                navController.navigateToIconFullscreen(icon, version)
            })
            iconFullscreen(onBackPress = { navController.popBackStack() })
            items()
            spells()
            runes()
            encyclopediaSettings(onLicenseClick = { navController.navigateToLicences() })
            licencesScreen(onBackPress = { navController.popBackStack() })
        }
    }
}

fun resolveStartDestination(lastNavigationRoute: String?): String {
    val screen = lastNavigationRoute?.let {
        Screen.allScreens.find { it.name == lastNavigationRoute }
    }
    return screen?.route ?: Screen.allScreens.first().route
}