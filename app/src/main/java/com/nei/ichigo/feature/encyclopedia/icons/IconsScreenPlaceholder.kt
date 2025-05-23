package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.ui.Screen

@Composable
fun IconsScreenPlaceholder() {
    Box(Modifier.systemBarsPadding()) {
        Surface(
            Modifier
                .padding(32.dp),
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Screen.Champions.icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text("Select a champion")
            }
        }
    }
}

@Preview
@Composable
fun IconsScreenPlaceholderPreview() {
    IconsScreenPlaceholder()
}