@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)

package com.red.code015.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val pages = Page.values().toList()

@Composable
fun IchigoScaffold(
    scope: CoroutineScope = rememberCoroutineScope(),
    pager: @Composable (Page) -> Unit,
) {
    val pagerState = rememberPagerState(1)
    Scaffold(
        bottomBar = {
            IchigoNavigationBar(
                currentPage = pagerState.currentPage,
            ) { pageIndex ->
                scope.launch {
                    pagerState.scrollToPage(pageIndex)
                }
            }
        }
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            userScrollEnabled = false,
        ) { pageIndex ->
            if (pageIndex <= pages.size) {
                pager(pages[pageIndex])
            }
        }
    }
}

@Composable
private fun IchigoNavigationBar(
    currentPage: Int,
    scrollToPage: (Int) -> Unit,
) {
    MaterialTheme {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.975f)) {
            pages.forEachIndexed { index, page ->
                NavigationBarItem(
                    icon = { Icon(page.icon, null) },
                    label = { Text(stringResource(id = page.resId)) },
                    selected = currentPage == index,
                    onClick = { scrollToPage(index) }
                )
            }
        }
    }
}