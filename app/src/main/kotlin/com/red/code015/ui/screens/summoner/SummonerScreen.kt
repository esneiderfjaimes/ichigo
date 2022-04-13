@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.red.code015.ui.screens.summoner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.red.code015.data.model.toUI2
import com.red.code015.domain.PlatformID
import com.red.code015.ui.screens.summoner.SummonerViewModel.State.SectionSummoner
import com.red.code015.ui.screens.summoner.sections_summoner.Leagues
import com.red.code015.ui.screens.summoner.sections_summoner.ToolBar
import com.red.code015.utils.toast

val paddingHorizontal = 32.dp
val margin = 8.dp

@Composable
fun SummonerScreen(
    platform: PlatformID,
    onBackPress: () -> Unit,
    summonerName: String,
    viewModel: SummonerViewModel = hiltViewModel(),
) {
    if (summonerName.isBlank()) {
        onBackPress()
        return
    }

    LaunchedEffect(Unit) {
        viewModel.setup(platform, summonerName)
    }

    val state by viewModel.state.collectAsState()

    SummonerScreen(
        onBackPress = onBackPress,
        summonerName = summonerName,
        throwable = state.throwable,
        sectionSummoner = state.sectionSummoner
    )
}

@Composable
private fun SummonerScreen(
    onBackPress: () -> Unit,
    summonerName: String,
    throwable: Throwable?,
    sectionSummoner: SectionSummoner,
) {
    Scaffold(
        topBar = { SummonerTopAppBar(summonerName = summonerName, onBackPress = onBackPress) },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(margin)
            ) {
                SectionSummoner(sectionSummoner)
            }
        }
    )

    if (throwable != null) {
        LocalContext.current.toast(throwable.message)
    }
}

@Composable
private fun ColumnScope.SectionSummoner(state: SectionSummoner) {
    when (state) {
        SectionSummoner.Loading -> CircularProgressIndicator(Modifier.align(CenterHorizontally))
        is SectionSummoner.Show -> state.run {
            ToolBar(sectionSummonerUI)
            Leagues(leagues = sectionSummonerUI.leagues.map { it.toUI2() })
        }
    }
}
