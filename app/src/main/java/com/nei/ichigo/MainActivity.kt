package com.nei.ichigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsScreen
import com.nei.ichigo.ui.theme.IchigoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IchigoTheme {
                ChampionsScreen()
            }
        }
    }
}