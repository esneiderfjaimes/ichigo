package com.red.code015.ui.pages.home.screens.register

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.red.code015.data.model.Platform

@Composable
fun RegisterTopAppBar(
    onBackPress: () -> Unit,
    platform: Platform,
    onPlatformClick: () -> Unit,
) {
    SmallTopAppBar(
        title = { Text("Register") },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            TextButton(onClick = onPlatformClick) {
                Text(text = platform.id.name)
            }
        }
    )
}