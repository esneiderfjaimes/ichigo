package com.nei.ichigo.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.nei.ichigo.core.designsystem.component.loadImageLoaderFactory
import com.nei.ichigo.navigation.IchigoNavHost
import com.nei.ichigo.navigation.IchigoNavSuite

@Composable
fun IchigoApp() {
    loadImageLoaderFactory()

    val navController = rememberNavController()
    IchigoNavSuite(navController) {
        IchigoNavHost(navController)
    }
}