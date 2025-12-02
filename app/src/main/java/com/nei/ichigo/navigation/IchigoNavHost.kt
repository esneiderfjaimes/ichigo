package com.nei.ichigo.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
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
import com.nei.ichigo.feature.encyclopedia.icons.navigation.IconsRoute
import com.nei.ichigo.feature.encyclopedia.icons.navigation.icons
import com.nei.ichigo.feature.encyclopedia.items.navigation.ItemsRoute
import com.nei.ichigo.feature.encyclopedia.items.navigation.items
import com.nei.ichigo.feature.encyclopedia.runes.navigation.RunesRoute
import com.nei.ichigo.feature.encyclopedia.runes.navigation.runes
import com.nei.ichigo.feature.encyclopedia.skin.fullscreen.navigation.navigateToSkinFullscreen
import com.nei.ichigo.feature.encyclopedia.skin.fullscreen.navigation.skinFullscreen
import com.nei.ichigo.feature.encyclopedia.spells.navigation.SpellsRoute
import com.nei.ichigo.feature.encyclopedia.spells.navigation.spells
import com.nei.ichigo.feature.licenses.navigation.licences
import com.nei.ichigo.feature.licenses.navigation.navigateToLicences
import com.nei.ichigo.feature.settings.navigation.EncyclopediaSettingsRoute
import com.nei.ichigo.feature.settings.navigation.encyclopediaSettings

data class NavBarItem(
    @StringRes
    val title: Int,
    val icon: ImageVector,
)

val TOP_LEVEL_ROUTES_LIST = listOf(
    ChampionsRoute to NavBarItem(
        title = R.string.champions,
        icon = IchigoIcons.Champion,
    ),
    IconsRoute to NavBarItem(
        title = R.string.icons,
        icon = IchigoIcons.ProfileIcons,
    ),
    ItemsRoute to NavBarItem(
        title = R.string.items,
        icon = IchigoIcons.Item,
    ),
    SpellsRoute to NavBarItem(
        title = R.string.spells,
        icon = IchigoIcons.Spell,
    ),
    RunesRoute to NavBarItem(
        title = R.string.runes,
        icon = IchigoIcons.Rune,
    ),
    EncyclopediaSettingsRoute to NavBarItem(
        title = R.string.settings,
        icon = IchigoIcons.Settings,
    ),
)

val TOP_LEVEL_ROUTES: Map<NavKey, NavBarItem> = TOP_LEVEL_ROUTES_LIST.toMap()

const val LIST_DETAIL_SCENE_MODE = true

@Composable
fun IchigoNavHost(
    navigationState: NavigationState,
    navigator: Navigator
) {
    val entryProvider = entryProvider {
        champions(onChampionClick = navigator::navigateToChampion)
        champion(
            onBackPress = navigator::goBack,
            navToSkinFullscreen = navigator::navigateToSkinFullscreen
        )
        skinFullscreen(onBackPress = navigator::goBack)
        icons()
        items()
        spells()
        runes()
        encyclopediaSettings(onLicenseClick = navigator::navigateToLicences)
        licences(onBackPress = navigator::goBack)
    }

    val sceneStrategy = if (LIST_DETAIL_SCENE_MODE) {
        rememberListDetailSceneStrategy<NavKey>()
    } else {
        SinglePaneSceneStrategy()
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = navigator::goBack,
        sceneStrategy = sceneStrategy
    )
}
