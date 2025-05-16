package com.nei.ichigo.feature.encyclopedia.champions.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.automirrored.rounded.Segment
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ItemCombo

@Composable
fun VersionDialog(
    selectedVersion: String?,
    versions: List<String>,
    onVersionSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        VersionDialogContent(
            selectedVersion = selectedVersion,
            versions = versions,
            onVersionSelected = onVersionSelected,
        )
    }
}

enum class ContentMode {
    List,
    Group
}

@Composable
fun VersionDialogContent(
    selectedVersion: String?,
    versions: List<String>,
    onVersionSelected: (String?) -> Unit,
) {
    var contentMode by rememberSaveable { mutableStateOf(ContentMode.List) }
    Surface(
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            Modifier
                .sizeIn(maxHeight = 600.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.select_version),
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(12.dp)
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    contentMode = if (contentMode == ContentMode.List) {
                        ContentMode.Group
                    } else {
                        ContentMode.List
                    }
                }) {
                    Icon(
                        if (contentMode == ContentMode.List) Icons.AutoMirrored.Rounded.Segment
                        else Icons.AutoMirrored.Rounded.List,
                        contentDescription = null
                    )
                }
            }

            when (contentMode) {
                ContentMode.List -> {
                    val lazyListState = rememberLazyListState()
                    LaunchedEffect(Unit) {
                        if (selectedVersion == null) {
                            lazyListState.animateScrollToItem(0)
                        } else {
                            val indexOf = versions.indexOf(selectedVersion)
                            if (indexOf == -1) return@LaunchedEffect
                            lazyListState.animateScrollToItem(indexOf + 1)
                        }
                    }

                    LazyColumn(
                        state = lazyListState,
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        item(key = null) {
                            ItemCombo(
                                value = stringResource(R.string.latest),
                                selected = selectedVersion == null,
                                onClick = { onVersionSelected(null) }
                            )
                        }
                        contentList(
                            versions = versions,
                            selectedVersion = selectedVersion,
                            onVersionSelected = onVersionSelected
                        )
                    }
                }

                ContentMode.Group -> {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        ItemCombo(
                            value = stringResource(R.string.latest),
                            selected = selectedVersion == null,
                            onClick = { onVersionSelected(null) }
                        )
                        ContentGroup(
                            versions = versions,
                            selectedVersion = selectedVersion,
                            onVersionSelected = onVersionSelected
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

fun LazyListScope.contentList(
    versions: List<String>,
    selectedVersion: String?,
    onVersionSelected: (String?) -> Unit
) {
    items(versions, key = { it }) { version ->
        ItemCombo(
            value = version,
            selected = version == selectedVersion,
            onClick = { onVersionSelected(version) }
        )
    }
}

@Composable
fun ContentGroup(
    versions: List<String>,
    selectedVersion: String?,
    onVersionSelected: (String?) -> Unit
) {
    var expandedGroupId by rememberSaveable { mutableStateOf<Int?>(null) }
    val groupBy = versions
        .groupBy { it.substringBefore(".").toInt() }
        .toSortedMap(compareByDescending { it })
    groupBy.forEach { (header, versions) ->
        Surface(
            shape = MaterialTheme.shapes.large,
            onClick = {
                expandedGroupId = if (expandedGroupId == header) {
                    null
                } else {
                    header
                }
            },
            tonalElevation = if (expandedGroupId == header) 2.dp else 0.dp
        ) {
            Column {
                Header(
                    value = header.toString(),
                    isExpanded = expandedGroupId == header
                )
                AnimatedVisibility(expandedGroupId == header) {
                    Column {
                        versions.forEach { version ->
                            ItemCombo(
                                value = version,
                                selected = version == selectedVersion,
                                onClick = { onVersionSelected(version) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(value: String, isExpanded: Boolean) {
    Row(
        Modifier
            .minimumInteractiveComponentSize()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
            contentDescription = null,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 24.dp)
        )
    }
}

@Preview
@Composable
fun VersionDialogContentPreview() {
    VersionDialogContent(
        selectedVersion = "1.0.0",
        versions = listOf(
            "1.0.0",
            "2.0.0",
            "3.0.0",
            "4.0.0",
            "5.0.0",
            "6.0.0",
            "7.0.0",
            "8.0.0",
            "9.0.0",
            "10.0.0"
        ),
        onVersionSelected = {}
    )
}