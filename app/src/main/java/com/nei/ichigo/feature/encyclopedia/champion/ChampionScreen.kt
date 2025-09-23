package com.nei.ichigo.feature.encyclopedia.champion

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.UiState
import com.nei.ichigo.common.toSuccessUiState
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImageDefaults
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.IchigoFilterChip
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.parallaxLayoutModifier
import com.nei.ichigo.core.designsystem.theme.Gold
import com.nei.ichigo.core.designsystem.theme.IchigoTheme
import com.nei.ichigo.core.designsystem.utils.getChampionImage
import com.nei.ichigo.core.designsystem.utils.getChampionSkinImage
import com.nei.ichigo.core.designsystem.utils.roleToString
import com.nei.ichigo.core.model.ChampionDetail
import com.nei.ichigo.core.model.Skin
import com.nei.ichigo.feature.encyclopedia.champion.ChampionViewModel.ChampionUiState

@Composable
fun ChampionScreen(
    championId: String,
    navToSkinFullscreen: (String?) -> Unit,
    onBackPress: () -> Unit
) {
    val viewModel = hiltViewModel<ChampionViewModel, ChampionViewModel.Factory>(
        key = championId,
    ) { factory ->
        factory.create(championId)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChampionScreen(
        state = state,
        navToSkinFullscreen = navToSkinFullscreen,
        onBackPress = onBackPress
    )
}

private val ITEM_SIZE = 80.dp
private val BORDER_SIZE = 2.dp

@Composable
private fun ChampionScreen(
    state: UiState<out ChampionUiState>,
    navToSkinFullscreen: (String?) -> Unit = {},
    onBackPress: () -> Unit = {}
) {
    BaseScreen(
        state = state,
        topBar = {
            TransparentTopAppBar(
                navigationIcon = {
                    FilledTonalIconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                },
                alpha = 0f
            )
        },
    ) { state, innerPadding ->
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
            onSkinClick = navToSkinFullscreen
        )
    }
}

private val MAX_WIDTH = 800.dp
// = Dp.Unspecified

@Composable
fun ChampionContent(
    modifier: Modifier,
    champion: ChampionDetail,
    version: String,
    onSkinClick: (String?) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header(
            modifier = Modifier.parallaxLayoutModifier(scrollState, 1.5),
            champion = champion,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                Modifier
                    .sizeIn(maxWidth = MAX_WIDTH)
                    .padding(horizontal = 32.dp),
                color = Gold,
                thickness = BORDER_SIZE
            )

            Text(
                text = champion.title.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .sizeIn(maxWidth = MAX_WIDTH)
                    .padding(4.dp)
            )

            Spacer(Modifier.height(16.dp))

            IchigoItemImage(
                model = getChampionImage(champion.image, version),
                modifier = Modifier.sizeIn(maxWidth = MAX_WIDTH),
                size = ITEM_SIZE,
                shape = CircleShape,
                borderWidth = BORDER_SIZE
            )

            Spacer(Modifier.height(16.dp))

            TagsSection(
                modifier = Modifier.sizeIn(maxWidth = MAX_WIDTH),
                tags = champion.tags,
                extraTag = champion.parType
            )

            Spacer(Modifier.height(16.dp))

            AboutSection(
                modifier = Modifier.sizeIn(maxWidth = MAX_WIDTH),
                text = champion.lore
            )

            Spacer(Modifier.height(16.dp))

            SkinsSection(
                modifier = Modifier.sizeIn(maxWidth = MAX_WIDTH),
                skins = champion.skins,
                championId = champion.id,
                onSkinClick = onSkinClick
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    champion: ChampionDetail
) {
    Box(
        modifier = Modifier,
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                //  .sizeIn(maxWidth = 600.dp, maxHeight = 600.dp)
                .padding(bottom = 16.dp)
                .aspectRatio(1215f / 717f)
                .then(modifier),
            model = getChampionSkinImage(champion.id, 0),
            loading = AsyncImageDefaults.LoadingCircleProgress,
            contentScale = ContentScale.FillHeight
        )

        Text(
            text = champion.name,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .statusBarsPadding()
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
}

@Composable
fun TagsSection(modifier: Modifier = Modifier, tags: List<String>, extraTag: String) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        tags.forEach { tag ->
            IchigoFilterChip(
                text = roleToString(tag),
                selected = false,
                onClick = {}
            )
        }

        IchigoFilterChip(
            text = extraTag,
            selected = false,
            onClick = {}
        )
    }
}

@Composable
fun AboutSection(modifier: Modifier = Modifier, text: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Surface(
        modifier = modifier
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
                text = text,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SkinsSection(
    modifier: Modifier = Modifier,
    skins: List<Skin>, championId: String, onSkinClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.skins),
            style = MaterialTheme.typography.titleLarge,
        )
    }
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { skins.count() },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        preferredItemWidth = 250.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp),
    ) { index ->
        val skin = skins.getOrNull(index)
            ?: return@HorizontalMultiBrowseCarousel
        AsyncImage(
            modifier = Modifier
                .maskClip(MaterialTheme.shapes.extraLarge)
                .clickable {
                    onSkinClick(skin.id)
                },
            model = getChampionSkinImage(championId, skin.num),
        )
    }
}

@PreviewLightDark
@Composable
fun ChampionScreenPreview() {
    IchigoTheme {
        AsyncImagePreviewProvider(
            width = 1215 / 2,
            height = 717 / 2
        ) {
            ChampionScreen(
                state = ChampionUiState(
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
                    ),
                    version = "1.0.0",
                ).toSuccessUiState()
            )
        }
    }
}