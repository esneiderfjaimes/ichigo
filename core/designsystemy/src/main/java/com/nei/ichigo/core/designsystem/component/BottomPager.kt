package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.core.designsystem.R

@Stable
data class PageInfo(
    val pageIndex: Int,
    val totalPages: Int,
)

@Composable
fun BottomPager(
    pageInfo: PageInfo,
    title: @Composable () -> String = { stringResource(R.string.core_designsystemy_select_page) },
    itemLabel: @Composable (Int) -> String = { (it + 1).toString() },
    onSelectPage: (Int) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
    ) {
        HorizontalFloatingToolbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            expanded = true
        ) {
            IconButton(
                onClick = {
                    onSelectPage(pageInfo.pageIndex - 1)
                },
                enabled = pageInfo.pageIndex > 0
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = null
                )
            }

            var showPageDialog by remember { mutableStateOf(false) }
            TextButton(onClick = {
                showPageDialog = true
            }) {
                Text(
                    text = stringResource(
                        id = R.string.core_designsystemy_page_info,
                        pageInfo.pageIndex + 1,
                        pageInfo.totalPages
                    )
                )
            }
            if (showPageDialog) {
                PagesDialog(
                    pageInfo = pageInfo,
                    title = title,
                    onSelectPage = onSelectPage,
                    itemLabel = itemLabel,
                    onDismiss = { showPageDialog = false }
                )
            }

            IconButton(
                onClick = {
                    onSelectPage(pageInfo.pageIndex + 1)
                },
                enabled = pageInfo.pageIndex < pageInfo.totalPages - 1
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun PagesDialog(
    pageInfo: PageInfo,
    onSelectPage: (Int) -> Unit,
    title: @Composable () -> String,
    itemLabel: @Composable (Int) -> String,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        PagesDialogContent(
            pageInfo = pageInfo,
            title = title,
            onSelectPage = {
                onSelectPage(it)
                onDismiss()
            },
            itemLabel = itemLabel,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun PagesDialogContent(
    pageInfo: PageInfo,
    onSelectPage: (Int) -> Unit,
    title: @Composable () -> String = { stringResource(R.string.core_designsystemy_select_page) },
    itemLabel: @Composable (Int) -> String = { (it + 1).toString() },
    onDismiss: () -> Unit = {}
) {
    val indexes = (0..(pageInfo.totalPages - 1)).toList()
    IchigoDialogContent(
        onCloseRequest = onDismiss,
        title = { IchigoTitleDialog(text = title()) }
    ) {
        SelectListContent(
            selectedItem = pageInfo.pageIndex,
            items = indexes,
            itemLabel = itemLabel,
            onSelectItem = onSelectPage
        )
    }
}

@Preview
@Composable
fun PagesDialogPreview() {
    PagesDialogContent(
        pageInfo = PageInfo(0, 10),
        onSelectPage = {},
        itemLabel = { (it + 1).toString() },
        onDismiss = {}
    )
}

@Preview
@Composable
fun BottomPagerPreview() {
    BottomPager(pageInfo = PageInfo(0, 10), onSelectPage = {})
}