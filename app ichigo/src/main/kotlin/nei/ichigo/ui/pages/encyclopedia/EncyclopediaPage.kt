@file:OptIn(ExperimentalMaterial3Api::class)

package nei.ichigo.ui.pages.encyclopedia

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nei.ichigo.ui.pages.encyclopedia.EncyclopediaViewModel.EncyclopediaState
import nei.ichigo.data.modeles.ChampListItem
import nei.ichigo.ui.pages.encyclopedia.champs.ChampsGrid
import nei.ichigo.data.modeles.Image
import nei.ichigo.ui.theme.IchigoTheme

@Composable
fun EncyclopediaPage(
    viewModel: EncyclopediaViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    EncyclopediaPage(state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncyclopediaPage(state: EncyclopediaState) {
    Scaffold(topBar = {
        EncyclopediaTopBar(if (state is EncyclopediaState.Success) state.lastVersion else null)
    }) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            if (state is EncyclopediaState.Success) {
                ChampsGrid(
                    champs = state.champions,
                    footer = state.footer
                )
            }
        }
    }
}

@Composable
fun EncyclopediaTopBar(lastVersion: String?) {
    TopAppBar(title = {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Encyclopedia")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                    )
                ) {
                    if (lastVersion.isNullOrEmpty()) append("...")
                    else append(" v${lastVersion}")
                }
            },
            style = MaterialTheme.typography.headlineSmall,
        )
    })
}

@Preview(device = "id:pixel")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel")
@Composable
fun PreviewEncyclopediaPage() {
    IchigoTheme {
        EncyclopediaPage(
            state = EncyclopediaState.Success(
                "13.8.1", listOf(
                    ChampListItem("1", "Morgana", Image.default),
                    ChampListItem("1", "Rakan", Image.default),
                    ChampListItem("1", "Tahm Kench", Image.default),
                )
            )
        )
    }
}