@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class)

package com.red.code015.ui.pages.home.screens.summoner

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.red.code015.data.model.*
import com.red.code015.domain.PlatformID
import com.red.code015.ui.common.*
import com.red.code015.ui.components.CommonChampionThumbnail
import com.red.code015.ui.pages.home.screens.summoner.SummonerViewModel.State.SectionSummoner
import com.red.code015.ui.pages.home.screens.summoner.sections_summoner.Leagues
import com.red.code015.ui.pages.home.screens.summoner.sections_summoner.ToolBar
import com.red.code015.utils.formatPts

val paddingHorizontal = 32.dp
val paddingVertical = paddingHorizontal / 2
val margin = 8.dp

@Composable
fun SummonerScreen(
    platform: PlatformID,
    onBackPress: () -> Unit,
    summonerName: String,
    viewModel: SummonerViewModel = viewModel(),
    onMasteryMorePress: (PlatformID, String) -> Unit,
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
        sectionSummoner = state.sectionSummoner,
        masteries = state.masteries,
        onMasteryMorePress = { onMasteryMorePress(platform, summonerName) }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun SummonerScreen(
    onBackPress: () -> Unit,
    summonerName: String,
    throwable: Throwable?,
    sectionSummoner: SectionSummoner,
    masteries: List<MasteryUI>,
    onMasteryMorePress: () -> Unit,
) {
    var champBackground by rememberSaveable { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = { SummonerTopAppBar(summonerName = summonerName, onBackPress = onBackPress) },
        content = {
            LazyColumn(
                Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 80.dp),
            ) {
                item {
                    SectionSummoner(sectionSummoner, champBackground)
                }
                item {
                    SectionMastery(masteries,
                        onMasteryMorePress = onMasteryMorePress) { champBackground = it }
                }
                item {
                    History()
                }
            }
        }
    )

    if (throwable != null) {
        // LocalContext.current.toast(throwable.message?.substring(0..32) ?: "No msg")
    }
}

@Composable
fun LazyItemScope.History() {
    (0..2).forEach { _ -> MyCard(modFill.animateItemPlacement()) }
}

@Composable
private fun LazyItemScope.SectionSummoner(state: SectionSummoner, champBackground: String?) {
    when (state) {
        SectionSummoner.Loading -> LoadingScreen()
        is SectionSummoner.Show -> state.run {
            ToolBar(summoner, champBackground)
            Leagues(leagues = summoner.leagues)
        }
    }
}

@Composable
private fun LazyItemScope.SectionMastery(
    masteries: List<MasteryUI>,
    onMasteryMorePress: () -> Unit,
    updateChampBackground: (String) -> Unit,
) {
    if (masteries.isEmpty()) return

    // TODO safe in preferences
    updateChampBackground(
        masteries.getBackgroundId(
            random = true,
            bgID = BgID.Lasted
        )
    )

    MyCard(modFill.animateItemPlacement()) {
        LazyRow(Modifier
            .fillMaxWidth()
            .padding(vertical = margin),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                MyIconButton(Rounded.ArrowBackIos, onClick = onMasteryMorePress)
            }
            item {
                ChampItem(mastery = masteries.getOrNull(1), percentage = 0.85f)
            }
            item {
                ChampItem(mastery = masteries.getOrNull(0))
            }
            item {
                ChampItem(mastery = masteries.getOrNull(2), percentage = 0.75f)
            }
            item {
                MyIconButton(Rounded.ArrowForwardIos, onClick = onMasteryMorePress)
            }
        }
    }
}

@Composable
fun ChampItem(
    modifier: Modifier = Modifier,
    mastery: MasteryUI?,
    @FloatRange(from = 0.0, to = 1.0)
    percentage: Float = 1f,
) {
    val size = 80.dp * percentage
    val alpha = -1.5f * percentage + 1.5f

    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(if (mastery == null) 4.dp else 0.dp)
    ) {
        if (mastery != null) ChampThumbMasterySummoner(
            champ = mastery.champListItem,
            size = size,
            masteryId = mastery.getMastery(),
            alpha = alpha
        )
        else Box(Modifier
            .placeholderDefault(true)
            .size(size))

        Text(text = mastery?.champListItem?.name ?: "name",
            Modifier.placeholderDefault(mastery == null),
            fontWeight = FontWeight.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center)

        Text(text = mastery?.championPoints?.formatPts() ?: "100 pts",
            Modifier.placeholderDefault(mastery == null),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center)
    }
}

@Composable
fun ChampThumbMasterySummoner(
    champ: ChampListItemUI,
    size: Dp,
    @DrawableRes masteryId: Int,
    alpha: Float = 0f,
    onClick: () -> Unit = {},
) {
    CommonChampionThumbnail(
        champ, Modifier.champThumbnailSum2(onClick, size),
    ) { imageBox ->
        Box(Modifier
            .width(size)
            .height(size + size / 3)
        ) {
            imageBox()
            Image(painter = painterResource(masteryId),
                modifier = Modifier
                    .size(size / 2)
                    .align(BottomCenter),
                contentDescription = null)
            Box(Modifier
                .fillMaxSize()
                .background(colorScheme.background.copy(alpha = alpha)))
        }
    }
}

fun Modifier.champThumbnailSum2(onClick: () -> Unit = {}, size: Dp) = size(size)
    .border(
        width = 1.75.dp,
        color = Color(0xFFC28F2C),
        shape = CircleShape
    )
    .padding(0.75.dp)
    .clip(CircleShape)
    .clickable(onClick = onClick)