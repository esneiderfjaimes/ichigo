package com.red.code015.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.red.code015.data.model.Platform
import com.red.code015.data.model.Platforms
import com.red.code015.ui.components.BottomSheet
import com.red.code015.ui.components.material_modifications.ModalBottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetPlatforms(
    state: ModalBottomSheetState,
    scope: CoroutineScope,
    selectedPlatform: Platform,
    onPlatformChange: (Platform) -> Unit,
) {
    BottomSheet(
        state = state,
        sheetContent = {
            ListPlatforms(selectedPlatform = selectedPlatform, onCloseClick = {
                scope.launch { state.hide() }
            }, onPlatformClick = {
                if (it != selectedPlatform)
                    onPlatformChange(it)
                scope.launch { state.hide() }
            })
        }
    )
}

@Composable
fun ListPlatforms(
    platforms: List<Platform> = Platforms,
    selectedPlatform: Platform? = null,
    onCloseClick: () -> Unit,
    onPlatformClick: (Platform) -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        LazyColumn {

            item {
                Box(Modifier.fillMaxSize()) {
                    Text(text = "Select Region",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                    IconButton(onClick = onCloseClick, Modifier.align(Alignment.CenterEnd)) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                        )
                    }
                }
            }

            items(platforms) { platform ->
                val isSelected = platform == selectedPlatform
                Box(modifier = Modifier
                    .clickable(
                        onClick = { onPlatformClick.invoke(platform) },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(true)
                    )
                    .background(
                        if (!isSelected) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
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

                    Text(
                        text = stringResource(platform.resIdName),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}