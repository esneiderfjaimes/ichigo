package com.nei.ichigo.feature.encyclopedia.champion

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.theme.Gold
import com.nei.ichigo.core.designsystem.utils.getChampionImage
import com.nei.ichigo.core.designsystem.utils.getChampionSkinImage
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
                                WindowInsetsSides.Start + WindowInsetsSides.End + WindowInsetsSides.Top
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
                    version = version
                )
            }
        }
    }
}

@Composable
fun ChampionContent(
    modifier: Modifier,
    champion: ChampionDetail,
    version: String
) {
    var selectedSkin by rememberSaveable { mutableStateOf<Int?>(null) }
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                //.align(Alignment.CenterHorizontally)
                .animateContentSize(),
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.Center),
                model = getChampionSkinImage(champion.id, 0),
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

        /*
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = getChampionImage(champion.image, version),
                modifier = Modifier
                    .clip(RoundedCornerShape(25))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = BORDER_SIZE,
                        color = Color(0xFFC28F2C),
                        shape = RoundedCornerShape(25)
                    )
                    .padding(BORDER_SIZE)
                    .size(ITEM_SIZE),
            )
            Column(
                Modifier.padding(horizontal = 16.dp),
            ) {
                Text(
                    text = champion.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = champion.title,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        */
        Spacer(Modifier.height(16.dp))
        AsyncImage(
            model = getChampionImage(champion.image, version),
            modifier = Modifier
                .padding(horizontal = 16.dp)
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
                    text = "About",
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
            text = "Skins",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { champion.skins.count() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp),
            preferredItemWidth = 250.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) { index ->
            val skin = champion.skins.getOrNull(index)
                ?: return@HorizontalMultiBrowseCarousel
            AsyncImage(
                modifier = Modifier
                    //.height(400.dp)
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .clickable {
                        selectedSkin = skin.num
                    },
                model = getChampionSkinImage(champion.id, skin.num),
            )
        }
        Spacer(Modifier.height(16.dp))
    }

    SkinFullscreen(
        championId = champion.id,
        currentSkinNum = selectedSkin,
        requestClose = { selectedSkin = null }
    )
}

// context(SharedTransitionScope)
@Composable
fun SkinFullscreen(
    championId: String,
    currentSkinNum: Int?,
    requestClose: () -> Unit
) {
    AnimatedContent(
        targetState = currentSkinNum,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "SkinFullscreen"
    ) { skinNum ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (skinNum != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = requestClose
                        )
                        .background(Color.Black.copy(alpha = 0.5f))
                )

                AsyncImage(
                    modifier = Modifier
                        .padding(32.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clickable(onClick = requestClose),
                    //.height(400.dp),
                    model = getChampionSkinImage(championId, skinNum),
                )

                BackHandler {
                    requestClose()
                }
            }
        }
    }
}


@Preview
@Composable
fun ChampionScreenPreview() {
    AsyncImagePreviewProvider(
        width = 1215,
        height = 717
    ) {
        ChampionScreen(
            state = ChampionUiState.Success(
                version = "1.0.0",
                champion = ChampionDetail(
                    id = "Aatrox",
                    name = "Aatrox",
                    skins = listOf(
                        Skin(id = "1", num = 1, name = "Aatrox", chromas = false),
                        Skin(id = "2", num = 2, name = "Aatrox", chromas = false)
                    ),
                    image = "",
                    tags = emptyList(),
                    title = "Title",
                    blurb = "Blurb",
                    parType = "ParType",
                    stats = mapOf(),
                    lore = "Lore"
                )
            ),
        )
    }
}