package com.red.code015.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.red.code015.data.model.Platform
import com.red.code015.data.model.Platforms
import com.red.code015.ui.components.material_modifications.ModalBottomSheetLayout
import com.red.code015.ui.components.material_modifications.ModalBottomSheetState
import com.red.code015.ui.theme.IchigoTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    state: ModalBottomSheetState,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit = {},
) {
    ModalBottomSheetLayout(
        sheetState = state,
        modifier = Modifier.padding(0.dp),
        sheetPadding = PaddingValues(32.dp),
        sheetShape = RoundedCornerShape(32.dp),
        sheetContent = sheetContent,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListPlatforms(
    platforms: List<Platform> = Platforms,
    selectedPlatform: Platform? = null,
    onCloseClick: () -> Unit,
    onPlatformClick: (Platform) -> Unit,
) {
    LazyColumn(Modifier) {

        item {
            Box(Modifier.fillMaxSize()) {
                Text(text = "Select Region",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp))
                IchigoTheme {
                    IconButton(onClick = onCloseClick, Modifier.align(Alignment.CenterEnd)) {
                        Icon(Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        items(platforms) { region ->
            Box(Modifier
                .fillMaxSize()
                .clickable(
                    enabled = true,
                    onClick = { onPlatformClick.invoke(region) },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(true)
                )
                .background(
                    if (region == selectedPlatform) MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.1f)
                    else MaterialTheme.colorScheme.background
                )
                .fillMaxWidth()
            ) {

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.125f),
                    thickness = 0.5.dp
                )

                Text(stringResource(region.resIdName),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(12.dp))
            }
        }
    }
}