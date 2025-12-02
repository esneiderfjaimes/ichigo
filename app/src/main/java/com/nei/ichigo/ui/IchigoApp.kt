package com.nei.ichigo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.core.designsystem.component.loadImageLoaderFactory
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.navigation.IchigoNavHost
import com.nei.ichigo.navigation.IchigoNavSuite
import com.nei.ichigo.navigation.Navigator
import com.nei.ichigo.navigation.TOP_LEVEL_ROUTES
import com.nei.ichigo.navigation.rememberNavigationState

@Composable
fun IchigoApp(
    // TODO: Use the last navigation route
    lastNavigationRoute: String?,
    updateLastNavigationRoute: (NavKey) -> Unit
) {
    loadImageLoaderFactory()

    val navigationState = rememberNavigationState(
        startRoute = ChampionsRoute,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )
    val navigator = remember { Navigator(navigationState) }
    IchigoNavSuite(
        navigationState = navigationState,
        updateLastNavigationRoute = {
            navigator.navigate(it)
            updateLastNavigationRoute(it)
        }
    ) {
        IchigoNavHost(
            navigator = navigator,
            navigationState = navigationState
        )
    }
}