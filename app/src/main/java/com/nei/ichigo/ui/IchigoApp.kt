package com.nei.ichigo.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.nei.ichigo.core.designsystem.component.loadImageLoaderFactory
import com.nei.ichigo.navigation.IchigoNavHost
import com.nei.ichigo.navigation.IchigoNavSuite
import com.nei.ichigo.navigation.Screen

@Composable
fun IchigoApp(
    lastNavigationRoute: String?,
    updateLastNavigationRoute: (Screen) -> Unit
) {
    loadImageLoaderFactory()

    val navController = rememberNavController()
    IchigoNavSuite(navController, updateLastNavigationRoute) {
        IchigoNavHost(navController, lastNavigationRoute)
    }
}