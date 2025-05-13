package com.red.code015.ui.pages.home.screens.home.cards.mysummoner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.red.code015.data.model.LeagueSummaryUI.Companion.summaryRelevant
import com.red.code015.data.model.SummonerSummaryUI
import com.red.code015.data.model.copyUpdates
import com.red.code015.data.model.requiresUpdate
import com.red.code015.domain.Profile
import com.red.code015.ui.components.AsyncImage
import com.red.code015.ui.pages.home.screens.home.HomeViewModel.State.CardMySummoner
import com.red.code015.ui.pages.home.screens.home.HomeViewModel.State.CardMySummoner.Loading
import com.red.code015.ui.pages.home.screens.home.HomeViewModel.State.CardMySummoner.Show
import com.red.code015.ui.theme.icons.IconsLoL
import com.red.code015.ui.theme.icons.Rank
import com.red.code015.utils.Coil

@Composable
fun MySummonerSection(
    state: CardMySummoner,
    profile: Profile?,
    onRegisterClick: () -> Unit = {},
    onDetailClick: (String) -> Unit = {},
    onRemoveClick: () -> Unit = {},
    updateProfile: (Profile) -> Unit = {},
) {
    Box(Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp, 16.dp)) {
        if (profile != null) when (state) {
            Loading -> Loading()
            is Show -> {
                val summonerUI = state.summonerUI
                if (profile.requiresUpdate(summonerUI)) {
                    updateProfile(profile.copyUpdates(summonerUI))
                }
                MySummoner(state.isLoading,
                    summoner = summonerUI,
                    onDetailClick = onDetailClick,
                    onRemoveClick = onRemoveClick)
            }
        }
        else UnregisteredSummoner(onRegisterClick)
    }
}

@Composable
private fun BoxScope.Loading() {
    CircularProgressIndicator(Modifier.align(Alignment.Center))
}

@Composable
private fun BoxScope.MySummoner(
    isLoading: Boolean,
    summoner: SummonerSummaryUI,
    onDetailClick: (String) -> Unit = {},
    onRemoveClick: () -> Unit = {},
) {
    val sizeSummonerIcon = 80.dp
    Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.height(sizeSummonerIcon)) {
                AsyncImage(modifier = Modifier
                    .size(sizeSummonerIcon)
                    .clip(RoundedCornerShape(33)),
                    model = Coil.urlProfileIcon(summoner.profileIconId),
                    loading = {
                        Box(Modifier.size(sizeSummonerIcon)) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                )
                if (summoner.level > 0) {
                    Text(text = summoner.level.toString(),
                        Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                            .padding(8.dp, 2.dp)
                            .align(Alignment.BottomCenter),
                        style = MaterialTheme.typography.bodySmall)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = summoner.name,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleLarge)

                if (summoner.account != null)
                    Text(text = "${summoner.account.gameName}#${summoner.account.tagLine}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodySmall)

                if (summoner.leagues.isNotEmpty()) {
                    val leaguePoints by rememberSaveable {
                        mutableStateOf(summoner.leagues.minOrNull()?.points)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(IconsLoL.Rank, contentDescription = "Rank Icon")
                        Text(text = summoner.leagues.summaryRelevant(leaguePoints),
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            Button(onClick = { onDetailClick(summoner.name) },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "Summoner Detail")
            }
        }
    }
    IconButton(onClick = onRemoveClick, Modifier.align(Alignment.TopEnd)) {
        Icon(Icons.Default.Close, contentDescription = "Remove summoner")
    }
}

@Composable
private fun BoxScope.UnregisteredSummoner(goToRegister: () -> Unit) {
    Button(onClick = goToRegister, modifier = Modifier.align(Alignment.Center)) {
        Text(text = "Register Summoner")
    }
}