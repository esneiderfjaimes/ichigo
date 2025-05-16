package com.nei.ichigo.feature.encyclopedia.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ItemCombo
import com.nei.ichigo.core.designsystem.utils.languageCodeToString

@Composable
fun LanguageDialog(
    selectedLanguage: String?,
    languages: List<String>,
    onLanguageSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        LanguageDialogContent(
            selectedLanguage = selectedLanguage,
            languages = languages,
            onLanguageSelected = onLanguageSelected,
        )
    }
}

@Composable
fun LanguageDialogContent(
    selectedLanguage: String?,
    languages: List<String>,
    onLanguageSelected: (String?) -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            Modifier
                .sizeIn(maxHeight = 600.dp)
        ) {
            Text(
                text = stringResource(R.string.select_language),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(24.dp)
            )

            val lazyListState = rememberLazyListState()

            LaunchedEffect(Unit) {
                if (selectedLanguage == null) {
                    lazyListState.animateScrollToItem(0)
                } else {
                    val indexOf = languages.indexOf(selectedLanguage)
                    if (indexOf == -1) return@LaunchedEffect
                    lazyListState.animateScrollToItem(indexOf + 1)
                }
            }

            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                item(key = null) {
                    ItemCombo(
                        stringResource(R.string.automatic),
                        selected = selectedLanguage == null,
                        onClick = { onLanguageSelected(null) }
                    )
                }

                items(languages.sorted(), key = { it }) { languageCode ->
                    ItemCombo(
                        value = languageCodeToString(languageCode),
                        selected = languageCode == selectedLanguage,
                        onClick = { onLanguageSelected(languageCode) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LanguageDialogContentPreview() {
    LanguageDialogContent(
        selectedLanguage = null,
        languages = listOf(
            "cs_CZ",
            "el_GR",
            "pl_PL",
            "ro_RO",
            "hu_HU",
            "en_GB",
            "de_DE",
            "es_ES",
            "it_IT",
            "fr_FR",
            "ja_JP",
            "ko_KR",
            "es_MX",
            "es_AR",
            "pt_BR",
            "en_US",
            "en_AU",
            "ru_RU",
            "tr_TR",
            "ms_MY",
            "en_PH",
            "en_SG",
            "th_TH",
            "vi_VN",
            "id_ID",
            "zh_MY",
            "zh_CN",
            "zh_TW",
        ),
        onLanguageSelected = {}
    )
}