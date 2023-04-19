package nei.ichigo.ui.pages.encyclopedia

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun EncyclopediaPage(state: EncyclopediaViewModel.EncyclopediaUiState) {
    Scaffold(topBar = {
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
                        if (state is EncyclopediaViewModel.EncyclopediaUiState.Success) append(" v${state.lastVersion}")
                        else append("...")
                    }
                },
                style = MaterialTheme.typography.headlineSmall,
            )
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (state is EncyclopediaViewModel.EncyclopediaUiState.Success)
                Text(text = state.champions.size.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEncyclopediaPage() {
    IchigoTheme {
        EncyclopediaPage(state = EncyclopediaViewModel.EncyclopediaUiState.Success("13.8.1"))
    }
}