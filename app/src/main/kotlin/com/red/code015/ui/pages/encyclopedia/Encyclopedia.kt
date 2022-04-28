@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)

package com.red.code015.ui.pages.encyclopedia

import android.annotation.SuppressLint
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.red.code015.ui.pages.encyclopedia.champions.ChampionScreen
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class EncyclopediaTab {
    Champions,
    Items
}

private val tabs = EncyclopediaTab.values().toList()

typealias TopBar = @Composable (
    @Composable (RowScope.() -> Unit), // Actions
    @Composable (ColumnScope.() -> Unit), // Header
) -> Unit

@Preview
@Composable
fun EncyclopediaPage(
    viewModel: EncyclopediaViewModel = hiltViewModel(),
) {
    val latestVersion by viewModel.latestVersion.collectAsState()
    if (latestVersion.isNotBlank())
        EncyclopediaPage(latestVersion)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun EncyclopediaPage(
    latestVersion: String,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val pagerState = rememberPagerState()

    // TODO: not working properly, possible solution:
    //  https://proandroiddev.com/scrollable-topappbar-with-jetpack-compose-bf22ca900cfe
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    val topBar: TopBar = { actions, header ->
        Surface(shadowElevation = 4.dp) {
            Column {
                SmallTopAppBar( // TODO: Make top app bar more small
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorScheme.surface.copy(0.75f)),
                    title = {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(
                                    color = colorScheme.primary,
                                    fontWeight = FontWeight.Bold)
                                ) {
                                    append("Encyclopedia")
                                }
                                if (latestVersion.isNotBlank()) withStyle(style = SpanStyle(
                                    color = colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontSize = typography.titleSmall.fontSize
                                )) {
                                    append(" v$latestVersion")
                                }
                            },
                            style = typography.headlineSmall,
                        )
                    },
                    actions = { actions() },
                    scrollBehavior = scrollBehavior
                )
                TabsEncyclopedia(
                    selectedTabIndex = pagerState.currentPage,
                    onSelectedTab = { scope.launch { pagerState.scrollToPage(it.ordinal) } }
                )
                header()
            }
        }
    }

    HorizontalPager(
        count = tabs.size,
        state = pagerState,
        userScrollEnabled = false
    ) { page ->
        when (tabs[page]) {
            EncyclopediaTab.Champions -> {
                ChampionScreen(topBar = topBar,
                    scrollBehavior = scrollBehavior,
                    latestVersion = latestVersion)
            }
            EncyclopediaTab.Items -> {
                Column {
                    topBar({
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Rounded.FilterList,
                                contentDescription = "Filter"
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Rounded.Sort,
                                contentDescription = "Filter"
                            )
                        }
                    }, {})
                }
            }

        }
    }
}

@Composable
private fun TabsEncyclopedia(selectedTabIndex: Int, onSelectedTab: (EncyclopediaTab) -> Unit) {
    TabRow(
        containerColor = Color.Transparent,
        selectedTabIndex = selectedTabIndex,
        indicator = {
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(it[selectedTabIndex])
                    .padding(horizontal = paddingHorizontal)
                    .clip(RoundedCornerShape(topStartPercent = 100, topEndPercent = 100)),
                height = 8.dp
            )
        },
        divider = { }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(33)),
                selected = index == selectedTabIndex,
                onClick = { onSelectedTab(tab) },
                text = { Text(text = tab.name) }
            )
        }
    }
}
