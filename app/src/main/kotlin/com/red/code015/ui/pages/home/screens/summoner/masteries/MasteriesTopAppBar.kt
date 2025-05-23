@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.home.screens.summoner.masteries

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.red.code015.ui.common.*
import com.red.code015.ui.pages.home.screens.summoner.masteries.Action.*
import com.red.code015.utils.clearFocusOnKeyboardDismiss2

val filterByChestGranted: List<Boolean?> = listOf(true, false)

enum class Action(val icon: ImageVector? = null) {
    Filter(Rounded.FilterList), Order(Rounded.Sort), Search(Rounded.Search), Refresh(Rounded.Refresh), SeeHow
}

enum class ShowView {
    Grid,
    List
}

val itemsMore = listOf(Search, Filter, Order, Refresh)

@Composable
fun MasteriesTopBar(
    onBackPress: () -> Unit,
    onShowViewClick: (ShowView) -> Unit,
    selectShowView: ShowView,
    showSheet: () -> Unit,
    onFiltersUpdate: (MasteriesViewModel.Filters) -> Unit,
    filters: MasteriesViewModel.Filters,
) {
    var expanded by remember { mutableStateOf(false) }
    var searchShowing by rememberSaveable { mutableStateOf(false) }
    var expandedAction by rememberSaveable { mutableStateOf<Action?>(null) }

    Column(Modifier.animateContentSize()) {
        SmallTopAppBar(
            title = {
                Text("Champion Mastery", style = typography.headlineSmall, maxLines = 2)
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Rounded.ArrowBackIosNew,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                FilledTonalButton(onClick = { showSheet() }) {
                    Text(text = "Booty")
                }
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Rounded.MoreVert, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.defaultMinSize(minWidth = 150.dp)
                ) {

                    DropdownMenuToggleItem(text = {
                        Text(text = "See how",
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }, items = mapOf(
                        ShowView.Grid to ChipData2("Grid", Rounded.GridView),
                        ShowView.List to ChipData2("List", Rounded.ViewList)
                    ), selected = selectShowView, onClick = onShowViewClick)

                    MenuDefaults.Divider()

                    itemsMore.forEach { action ->
                        if (action == Refresh) MenuDefaults.Divider()
                        DropdownMenuItem(text = {
                            Text(text = action.name)
                        }, onClick = {
                            if (action == Search) searchShowing = true
                            else expandedAction = action
                            expanded = false
                        }, leadingIcon = {
                            action.icon?.let {
                                Icon(imageVector = it, contentDescription = null)
                            }
                        }, enabled = action != Search || !searchShowing)
                    }
                }
            },
        )
        Header(
            searchShowing = searchShowing,
            filters = filters,
            onExpandLessClick = { searchShowing = false },
            onFiltersUpdate = onFiltersUpdate
        )
    }

    SheetFilters(
        expanded = expandedAction == Filter,
        onDismissRequest = { expandedAction = null },
        filters = filters,
        onFilterChange = onFiltersUpdate
    )

    SheetSorters(
        expanded = expandedAction == Order,
        onDismissRequest = { expandedAction = null },
        filters = filters,
        onFilterChange = onFiltersUpdate
    )
}

@Composable
private fun Header(
    searchShowing: Boolean,
    filters: MasteriesViewModel.Filters,
    onExpandLessClick: () -> Unit,
    onFiltersUpdate: (MasteriesViewModel.Filters) -> Unit,
) {
    if (searchShowing) OutlinedTextField(
        value = filters.search,
        leadingIcon = myIcon(Rounded.Search),
        trailingIcon = myIconButton(
            if (filters.search.isBlank()) Rounded.ExpandLess
            else Rounded.HighlightOff
        ) {
            if (filters.search.isBlank()) onExpandLessClick()
            else onFiltersUpdate(filters.copy(search = ""))
        },
        onValueChange = { onFiltersUpdate(filters.copy(search = it.trimStart())) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin)
            .clearFocusOnKeyboardDismiss2(),
        singleLine = true,
        placeholder = { Text(text = "Search champion") },
        shape = RoundedCornerShape(0)
    )
}
