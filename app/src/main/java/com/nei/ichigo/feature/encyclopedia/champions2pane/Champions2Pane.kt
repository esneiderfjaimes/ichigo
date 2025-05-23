package com.nei.ichigo.feature.encyclopedia.champions2pane

import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.encyclopedia.champion.ChampionScreen
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsScreen
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.icons.IconsScreenPlaceholder
import kotlinx.coroutines.launch

fun NavGraphBuilder.championsListDetail() {
    composable<ChampionsRoute> {
        Champions2PaneScreen()
    }
}

@Composable
fun Champions2PaneScreen() {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    fun onChampionClick(championId: String) {
        scope.launch {
            val newChampionId =
                if (scaffoldNavigator.currentDestination?.contentKey == championId) null
                else championId
            scaffoldNavigator.navigateTo(
                ListDetailPaneScaffoldRole.Detail,
                newChampionId
            )
        }
    }

    val paneExpansionState = rememberPaneExpansionState()

    LaunchedEffect(Unit) {
        scope.launch {
            paneExpansionState.setFirstPaneProportion(0.5f)
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                ChampionsScreen(
                    onChampionClick = ::onChampionClick
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let { championId ->
                    ChampionScreen(
                        championId = championId,
                        onBackPress = {
                            scope.launch {
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    )
                } ?: IconsScreenPlaceholder()
            }
        },
        paneExpansionState = paneExpansionState,
    )
}