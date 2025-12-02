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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.icon.Champion
import com.nei.ichigo.core.designsystem.icon.IchigoIcons

@Composable
fun IconsScreenPlaceholder(onClick: () -> Unit = {}) {
    Box(Modifier.systemBarsPadding()) {
        Surface(
            modifier = Modifier
                .padding(32.dp),
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.extraLarge,
            onClick = onClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = IchigoIcons.Champion,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text(stringResource(R.string.select_a_champion))
            }
        }
    }
}

@Preview
@Composable
fun IconsScreenPlaceholderPreview() {
    IconsScreenPlaceholder()
}