package com.nei.ichigo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.champions

@Composable
fun IchigoNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ChampionsRoute,
    ) {
        champions()
    }
}