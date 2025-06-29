package com.nei.ichigo.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ShortNavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.nei.ichigo.R

@Composable
fun IchigoNavSuite(navController: NavHostController, content: @Composable (() -> Unit)) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val navSuiteType = calculateFromAdaptiveInfo()
    var showMoreOptionsButton by rememberSaveable { mutableStateOf(false) }

    val allScreens = Screen.allScreens
    val navScreen = allScreens.take(3)
    val moreOptions = allScreens.drop(3)
    NavigationSuiteScaffold2(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        layoutType = navSuiteType,
        navigationSuiteColors = NavigationSuiteDefaults.colors(navigationBarContainerColor = Color.Transparent),
        containerColor = Color.Transparent,
        navigationItems = {
            navScreen.forEach { screen ->
                NavigationSuiteItem(
                    navigationSuiteType = navSuiteType,
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(screen.title)) },
                    selected = currentDestination?.destination?.route == screen.route,
                    onClick = { screen.action(navController) }
                )
            }

            NavigationSuiteItem(
                navigationSuiteType = navSuiteType,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.MoreHoriz,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.more)) },
                selected = showMoreOptionsButton,
                onClick = {
                    showMoreOptionsButton = true
                }
            )
        },
        content = content
    )
    if (showMoreOptionsButton) {
        ModalBottomSheet(
            onDismissRequest = {
                showMoreOptionsButton = false
            },
        ) {
            MoreOptionsBottomSheet(moreOptions, currentDestination) { screen ->
                showMoreOptionsButton = false
                screen.action(navController)
            }
        }
    }
}

/**
 * check [androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo]
 */
@Composable
@Suppress("DEPRECATION")
fun calculateFromAdaptiveInfo(): NavigationSuiteType {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    return with(adaptiveInfo) {
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> NavigationSuiteType.NavigationBar
            WindowWidthSizeClass.MEDIUM -> NavigationSuiteType.WideNavigationRailCollapsed
            WindowWidthSizeClass.EXPANDED -> NavigationSuiteType.WideNavigationRailExpanded
            else -> NavigationSuiteType.NavigationRail
        }
    }
}

/**
 * check [androidx.compose.material3.adaptive.navigationsuite.navigationSuiteScaffoldConsumeWindowInsets]
 */
@Composable
private fun Modifier.navigationSuiteScaffoldConsumeWindowInsets(
    navigationSuiteType: NavigationSuiteType,
    state: NavigationSuiteScaffoldState
): Modifier =
    consumeWindowInsets(
        if (state.currentValue == NavigationSuiteScaffoldValue.Hidden && !state.isAnimating) {
            WindowInsets(0, 0, 0, 0)
        } else {
            when (navigationSuiteType) {
                NavigationSuiteType.ShortNavigationBarCompact,
                NavigationSuiteType.ShortNavigationBarMedium ->
                    ShortNavigationBarDefaults.windowInsets.only(WindowInsetsSides.Bottom)

                NavigationSuiteType.WideNavigationRailCollapsed,
                NavigationSuiteType.WideNavigationRailExpanded,
                    ->
                    WideNavigationRailDefaults.windowInsets.only(WindowInsetsSides.Start)

                NavigationSuiteType.NavigationBar ->
                    NavigationBarDefaults.windowInsets.only(WindowInsetsSides.Bottom)

                NavigationSuiteType.NavigationRail ->
                    NavigationRailDefaults.windowInsets.only(WindowInsetsSides.Start)

                NavigationSuiteType.NavigationDrawer ->
                    DrawerDefaults.windowInsets.only(WindowInsetsSides.Start)

                else -> WindowInsets(0, 0, 0, 0)
            }
        }
    )

/**
 * check [androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold]
 */
@Composable
fun NavigationSuiteScaffold2(
    navigationItems: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    layoutType: NavigationSuiteType,
    navigationSuiteColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = NavigationSuiteScaffoldDefaults.containerColor,
    contentColor: Color = NavigationSuiteScaffoldDefaults.contentColor,
    state: NavigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState(),
    navigationItemVerticalArrangement: Arrangement.Vertical =
        NavigationSuiteDefaults.verticalArrangement,
    primaryActionContent: @Composable (() -> Unit) = {},
    primaryActionContentHorizontalAlignment: Alignment.Horizontal =
        NavigationSuiteScaffoldDefaults.primaryActionContentAlignment,
    content: @Composable () -> Unit,
) {
    Surface(modifier = modifier, color = containerColor, contentColor = contentColor) {
        NavigationSuiteScaffoldLayout(
            navigationSuite = {
                NavigationSuite(
                    // fix window insets navigation suite
                    modifier = Modifier.windowInsetsPadding(
                        when (layoutType) {
                            NavigationSuiteType.NavigationRail ->
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Start)

                            else -> WindowInsets(0, 0, 0, 0)
                        }
                    ),
                    navigationSuiteType = layoutType,
                    colors = navigationSuiteColors,
                    primaryActionContent = primaryActionContent,
                    verticalArrangement = navigationItemVerticalArrangement,
                    content = navigationItems
                )
            },
            navigationSuiteType = layoutType,
            state = state,
            primaryActionContent = primaryActionContent,
            primaryActionContentHorizontalAlignment = primaryActionContentHorizontalAlignment,
            content = {
                Box(
                    Modifier.navigationSuiteScaffoldConsumeWindowInsets(layoutType, state)
                ) {
                    content()
                }
            }
        )
    }
}

@Composable
fun MoreOptionsBottomSheet(
    moreOptions: List<Screen>,
    currentDestination: NavBackStackEntry?,
    onClick: (Screen) -> Unit
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        moreOptions.forEach { screen ->
            NavigationBarItem(
                modifier = Modifier
                    .sizeIn(maxWidth = 100.dp)
                    .width(75.dp),
                selected = currentDestination?.destination?.route == screen.route,
                onClick = { onClick(screen) },
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.title)) },
            )
        }
    }
}

@Preview
@Composable
fun IchigoAppPreview() {
    Surface(Modifier.width(175.dp)) {
        MoreOptionsBottomSheet(
            moreOptions = Screen.allScreens,
            currentDestination = null,
            onClick = {}
        )
    }
}

@PreviewScreenSizes
@Composable
private fun NavigationSuitePreview() {
    val navController = rememberNavController()
    IchigoNavSuite(navController = navController) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .size(48.dp)
                    .background(Color.Yellow)
                    .align(Alignment.TopStart)
            )
            Box(
                Modifier
                    .size(48.dp)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
            )
            Box(
                Modifier
                    .size(48.dp)
                    .background(Color.Green)
                    .align(Alignment.BottomEnd)
            )
            Box(
                Modifier
                    .size(48.dp)
                    .background(Color.Blue)
                    .align(Alignment.BottomStart)
            )
        }
    }
}