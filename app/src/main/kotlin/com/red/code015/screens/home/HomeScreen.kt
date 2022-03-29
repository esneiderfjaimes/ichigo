package com.red.code015.screens.home

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.red.code015.domain.SummonerSummary
import com.red.code015.screens.Destination
import com.red.code015.screens.home.sections.SummonerSummarySection

@OptIn(ExperimentalMaterial3Api::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    nav: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    viewModel.nav.observe(LocalLifecycleOwner.current) {
        when (it) {
            Destination.Register -> nav.navigate("register")
            Destination.Summoner -> Unit
            else -> Unit
        }
    }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }

    Scaffold(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HomeAppBar(scrollBehavior) },
        content = { innerPadding -> BodyContent(innerPadding, viewModel) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContent(innerPadding: PaddingValues, viewModel: HomeViewModel) {
    val event by viewModel.event.observeAsState()
    HomeHeader(innerPadding) {
        item { Spacer(Modifier.height(8.dp)) }

        item {
            SummonerSummarySection(state = HomeViewModel.State.Loading, viewModel)
        }
        item {
            SummonerSummarySection(state = HomeViewModel.State.SummonerNotFound(
                Throwable(),
                "Red Iense"), viewModel)
        }
        item {
            SummonerSummarySection(state = HomeViewModel.State.SummonerFound(
                SummonerSummary("Like you do", 3592, 500)), viewModel)
        }
        item {
            SummonerSummarySection(state = HomeViewModel.State.UnregisteredSummoner,
                viewModel)
        }

        item {
            SummonerSummarySection(state = event, viewModel)
        }
    }
}