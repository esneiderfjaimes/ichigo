@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.ichigo.feature.encyclopedia.champions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsViewModel.ChampionsUiState

@Composable
fun ChampionsScreen(viewModel: ChampionsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChampionsScreen(
        state = state
    )
}

@Composable
fun ChampionsScreen(state: ChampionsUiState) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(text = "Champions") }
        )
    }) { innerPadding ->
        when (state) {
            ChampionsUiState.Error -> {
                Text(text = "Error")
            }

            ChampionsUiState.Loading -> {
                Text(text = "Loading")
            }

            is ChampionsUiState.Success -> {
                ChampionsSuccess(
                    champions = state.champions,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ChampionsSuccess(champions: List<Champion>, modifier: Modifier = Modifier) {
    FlowRow(modifier = modifier) {
        champions.forEach { champion ->
            ChampionItem(champion)
        }
    }
}

@Composable
fun ChampionItem(champion: Champion) {
    Column {
        Text(champion.name)
    }
}

@Preview
@Composable
fun ChampionsScreenPreview() {
    ChampionsScreen(state = ChampionsUiState.Success(listOf()))
}