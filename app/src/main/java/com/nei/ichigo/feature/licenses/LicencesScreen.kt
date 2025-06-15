package com.nei.ichigo.feature.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.aboutlibraries.ui.compose.android.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.theme.IchigoTheme

@Composable
fun LicencesScreen(
    onBackPress: () -> Unit = {},
) {
    val libraries by rememberLibraries(R.raw.aboutlibraries)
    Scaffold(
        topBar = { LicencesTopBar(onBackPress) }
    ) { innerPadding ->
        when (libraries) {
            null -> {
                LoadingScreen()
            }

            else -> {
                LibrariesContainer(
                    libraries = libraries,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun LicencesTopBar(onBackPress: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Licences")
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = "Back")
            }
        }
    )
}

@Preview
@Composable
private fun LicencesScreenPreview() {
    IchigoTheme {
        LicencesScreen()
    }
}