package com.red.code015.screens.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.red.code015.domain.SummonerSummary
import com.red.code015.screens.home.HomeViewModel
import com.red.code015.screens.home.HomeViewModel.State.*
import com.red.code015.utils.Coil.urlProfileIcon

@Composable
fun SummonerSummarySection(state: HomeViewModel.State?, viewModel: HomeViewModel) {
    Box(Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp, 16.dp)) {
        when (state) {
            Loading -> LoadingState()
            is SummonerFound -> SummarySummonerState(state.summonerSummary,
                viewModel::summonerDetail,
                viewModel::removeSummoner)
            is SummonerNotFound ->
                SummonerNotFoundState(state.summonerName, viewModel::removeSummoner)
            UnregisteredSummoner ->
                UnregisteredSummonerState(viewModel::registerSummoner)
            else -> Unit
        }
    }
}

@Composable
fun BoxScope.LoadingState() {
    CircularProgressIndicator(Modifier.align(Alignment.Center))
}

@Composable
fun BoxScope.SummonerNotFoundState(summonerName: String, removeSummoner: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(33))
            .background(MaterialTheme.colorScheme.error)) {
            Icon(Icons.Rounded.Error, contentDescription = "Error",
                Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                MaterialTheme.colorScheme.onError)
        }
        Column {
            Text(text = summonerName,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge
            )
            Text(text = "Summoner not found")
        }
    }
    IconButton(onClick = removeSummoner, Modifier.align(Alignment.CenterEnd)) {
        Icon(Icons.Default.Close, contentDescription = "Remove summoner")
    }
}

@Composable
fun BoxScope.SummarySummonerState(
    summonerSummary: SummonerSummary,
    summonerDetail: () -> Unit,
    removeSummoner: () -> Unit,
) {
    val sizeSummonerIcon = 80.dp
    Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.height(sizeSummonerIcon)) {
                SubcomposeAsyncImage(urlProfileIcon(summonerSummary.profileIconId),
                    contentDescription = "Summoner Icon",
                    Modifier
                        .size(sizeSummonerIcon)
                        .clip(RoundedCornerShape(33)),
                    loading = {
                        Box(Modifier.size(sizeSummonerIcon)) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                )
                Text(text = summonerSummary.level.toString(),
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(8.dp, 2.dp)
                        .align(Alignment.BottomCenter),
                    style = MaterialTheme.typography.bodySmall)
            }
            Text(text = summonerSummary.name ?: "",
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge)
        }
        Button(onClick = summonerDetail, Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Summoner Detail")
        }
    }
    IconButton(onClick = removeSummoner, Modifier.align(Alignment.TopEnd)) {
        Icon(Icons.Default.Close, contentDescription = "Remove summoner")
    }
}

@Composable
fun BoxScope.UnregisteredSummonerState(registerMySummoner: () -> Unit) {
    Button(onClick = registerMySummoner, Modifier.align(Alignment.Center)) {
        Text(text = "Register Summoner")
    }
}