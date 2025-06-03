package com.nei.ichigo.feature.encyclopedia.champion

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.IchigoFilterChip
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.theme.Gold
import com.nei.ichigo.core.designsystem.utils.getChampionImage
import com.nei.ichigo.core.designsystem.utils.getChampionSkinImage
import com.nei.ichigo.core.designsystem.utils.roleToString
import com.nei.ichigo.core.model.ChampionDetail
import com.nei.ichigo.core.model.Skin
import com.nei.ichigo.feature.encyclopedia.champion.ChampionViewModel.ChampionUiState

@Composable
fun ChampionScreen(
    championId: String,
    onBackPress: () -> Unit,
) {
    ChampionScreen(
        viewModel = hiltViewModel<ChampionViewModel, ChampionViewModel.Factory>(
            key = championId,
        ) { factory ->
            factory.create(championId)
        },
        onBackPress = onBackPress
    )
}

@Composable
fun ChampionScreen(
    viewModel: ChampionViewModel,
    onBackPress: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChampionScreen(state = state, onBackPress = onBackPress)
}

private val ITEM_SIZE = 80.dp
private val BORDER_SIZE = 2.dp

@Composable
private fun ChampionScreen(state: ChampionUiState, onBackPress: () -> Unit = {}) {
    var selectedSkin by rememberSaveable { mutableStateOf<Int?>(null) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing
                            .only(
                                /*WindowInsetsSides.Start + WindowInsetsSides.End +*/
                                WindowInsetsSides.Top
                            )
                    ),
            ) {
                FilledTonalIconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = null
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        when (state) {
            ChampionUiState.Error -> {
                ErrorScreen(
                    Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                )
            }

            ChampionUiState.Loading -> {
                LoadingScreen(
                    Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                )
            }

            is ChampionUiState.Success -> {
                val champion = state.champion
                val version = state.version
                val layoutDirection = LocalLayoutDirection.current
                val contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
                ChampionContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    champion = champion,
                    version = version,
                    onSkinClick = { indexSkin ->
                        selectedSkin = indexSkin
                    }
                )
            }
        }
    }

    if (state is ChampionUiState.Success) {
        SkinFullscreen(
            championId = state.champion.id,
            skins = state.champion.skins,
            selectedSkin = selectedSkin,
            onSelectSkin = { indexSkin ->
                selectedSkin = indexSkin
            }
        )
    }
}

@Composable
fun ChampionContent(
    modifier: Modifier,
    champion: ChampionDetail,
    version: String,
    onSkinClick: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .animateContentSize(),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxWidth = 600.dp)
                    .padding(bottom = 16.dp)
                    .align(Alignment.Center),
                model = getChampionSkinImage(champion.id, 0),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = champion.name,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background,
                            )
                        ),
                    ),
                style = MaterialTheme.typography.headlineLarge
                    .copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = Gold
            )
        }
        HorizontalDivider(
            Modifier
                .sizeIn(maxWidth = 600.dp)
                .padding(horizontal = 32.dp)
                .align(Alignment.CenterHorizontally),
            color = Gold,
            thickness = BORDER_SIZE
        )
        Text(
            text = champion.title.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        )
        Spacer(Modifier.height(16.dp))
        AsyncImage(
            model = getChampionImage(champion.image, version),
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = BORDER_SIZE,
                    color = Gold,
                    shape = CircleShape
                )
                .padding(BORDER_SIZE)
                .size(ITEM_SIZE)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            champion.tags.forEach { tag ->
                IchigoFilterChip(
                    text = roleToString(tag),
                    selected = false,
                    onClick = {}
                )
            }

            IchigoFilterChip(
                text = champion.parType,
                selected = false,
                onClick = {}
            )
        }
        Spacer(Modifier.height(16.dp))
        var expanded by remember { mutableStateOf(false) }
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            onClick = { expanded = !expanded },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.about),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = champion.lore,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.skins),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { champion.skins.count() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            preferredItemWidth = 250.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp),
        ) { index ->
            val skin = champion.skins.getOrNull(index)
                ?: return@HorizontalMultiBrowseCarousel
            AsyncImage(
                modifier = Modifier
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .clickable { onSkinClick(index) },
                model = getChampionSkinImage(champion.id, skin.num),
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun ChampionScreenPreview() {
    AsyncImagePreviewProvider(
        width = 1215 / 2,
        height = 717 / 2
    ) {
        ChampionScreen(
            state = ChampionUiState.Success(
                version = "1.0.0",
                champion = ChampionDetail(
                    id = "Aatrox",
                    name = "Aatrox",
                    skins = listOf(
                        Skin(id = "1", num = 1, name = "Aatrox", chromas = false),
                        Skin(id = "2", num = 2, name = "Aatrox", chromas = false),
                        Skin(id = "3", num = 3, name = "Aatrox", chromas = false),
                    ),
                    image = "",
                    tags = listOf("Assassin", "Fighter"),
                    title = "Title",
                    parType = "Mana",
                    lore = "Lore",
                    allyTips = listOf(),
                    enemyTips = listOf(),
                )
            ),
        )
    }
}