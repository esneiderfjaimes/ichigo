package com.nei.ichigo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.champions
import com.nei.ichigo.feature.encyclopedia.icons.navigation.icons
import com.nei.ichigo.feature.encyclopedia.settings.navigation.encyclopediaSettings

@Composable
fun IchigoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ChampionsRoute,
    ) {
        champions()
        icons()
        encyclopediaSettings(onDismiss = { navController.popBackStack() })
    }
}