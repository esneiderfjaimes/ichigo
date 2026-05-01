@file:OptIn(ExperimentalLayoutApi::class)

package com.nei.ichigo.feature.encyclopedia.runes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.BaseTopAppBar
import com.nei.ichigo.common.UiState
import com.nei.ichigo.common.layout.defaultPaddingValues
import com.nei.ichigo.common.toSuccessUiState
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.utils.getRuneImage
import com.nei.ichigo.core.model.Rune
import com.nei.ichigo.core.model.RuneBranch
import com.nei.ichigo.core.model.RuneSlot
import com.nei.ichigo.feature.encyclopedia.runes.RunesViewModel.RunesUiState

@Composable
fun RunesScreen() {
    val viewModel = hiltViewModel<RunesViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RunesScreen(
        state = state,
    )
}

@Composable
private fun RunesScreen(
    state: UiState<out RunesUiState>,
) {
    BaseScreen(
        state = state,
        topBar = { RunesTopAppBar(state) },
    ) { state, innerPadding ->
        RunesContent(state = state, innerPadding = innerPadding)
    }
}

@Composable
private fun RunesContent(
    state: RunesUiState,
    innerPadding: PaddingValues,
) {
    val contentPadding = defaultPaddingValues(innerPadding)
    var currentItemId by rememberSaveable { mutableStateOf(state.branches.firstOrNull()?.id ?: "") }
    val currentBranch = state.branches.find { it.id == currentItemId }
    var currentRuneId by rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                state.branches.forEach { branch ->
                    RuneBranchItem(
                        branch = branch,
                        currentItemId = currentItemId,
                        onClick = { currentItemId = branch.id },
                    )
                }
            }

            currentBranch?.let { runeBranch ->
                Spacer(modifier = Modifier.size(16.dp))
                RuneTree(
                    branch = runeBranch,
                    currentRuneSelected = currentRuneId,
                    onRuneSelectedChange = { rune -> currentRuneId = rune?.id },
                )
            }
        }
    }
}

@Composable
private fun RuneBranchItem(
    modifier: Modifier = Modifier,
    branch: RuneBranch,
    currentItemId: String,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .sizeIn(minWidth = 90.dp),
        onClick = { onClick(branch.id) },
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        selected = currentItemId == branch.id,
        border = if (currentItemId == branch.id) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
    ) {
        Column(
            modifier = Modifier
                .padding(DEFAULT_ITEM_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = getRuneImage(branch.icon),
                modifier = Modifier.size(16.dp),
            )
            IchigoItemLabel(
                text = branch.name,
            )
        }
    }
}

@Composable
fun RuneTree(
    branch: RuneBranch,
    currentRuneSelected: String?,
    onRuneSelectedChange: (Rune?) -> Unit
) {
    branch.slots.forEach { slot ->
        FlowRow(
            horizontalArrangement = Arrangement.Center
        ) {
            slot.runes.forEach { rune ->
                RuneItem(
                    rune = rune,
                    currentRuneSelected = currentRuneSelected,
                    onRuneSelectedChange = onRuneSelectedChange
                )
            }
        }
    }
}

@Composable
fun RuneItem(
    rune: Rune,
    currentRuneSelected: String?,
    onRuneSelectedChange: (Rune?) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemImage(
            model = getRuneImage(rune.icon),
            modifier = Modifier
                .clickable { onRuneSelectedChange(rune) },
            size = 60.dp,
            shape = CircleShape,
        )

        RunesPopUp(
            currentItemId = currentRuneSelected,
            item = rune,
            onDismissRequest = { onRuneSelectedChange(null) },
        )
    }
}

@Composable
fun RunesTopAppBar(
    uiState: UiState<out RunesUiState>,
) {
    BaseTopAppBar(uiState, R.string.runes)
}

@Preview
@Composable
private fun SpellsScreenPreview() {
    AsyncImagePreviewProvider {
        RunesScreen(
            state = RunesUiState(
                branches = List(5) {
                    RuneBranch(
                        id = it.toString(),
                        name = "test $it",
                        icon = "test $it",
                        slots = List(4) {
                            RuneSlot(
                                runes = List(3) {
                                    Rune(
                                        id = it.toString(),
                                        name = "test $it",
                                        icon = "test $it",
                                        key = it.toString(),
                                        shortDesc = "test $it",
                                        longDesc = "test $it",
                                    )
                                }
                            )
                        },
                        key = it.toString()
                    )
                },
                version = "1.0.0",
            ).toSuccessUiState()
        )
    }
}