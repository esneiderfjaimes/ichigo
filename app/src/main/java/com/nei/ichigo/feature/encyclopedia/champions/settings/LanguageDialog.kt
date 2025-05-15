@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.ichigo.feature.encyclopedia.champions.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.core.designsystem.component.IchigoFilterChip


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
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Select Language",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IchigoFilterChip(
                    text = "Automatic",
                    selected = selectedLanguage == null,
                    onClick = {
                        onLanguageSelected(null)
                    }
                )

                languages.forEach { languageCode ->
                    IchigoFilterChip(
                        text = languageCodeToString(languageCode),
                        selected = languageCode == selectedLanguage,
                        onClick = {
                            onLanguageSelected(languageCode)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun languageCodeToString(languageCode: String): String {
    return when (languageCode) {
        "cs_CZ" -> "Czech (Czech Republic)"
        "el_GR" -> "Greek (Greece)"
        "pl_PL" -> "Polish (Poland)"
        "ro_RO" -> "Romanian (Romania)"
        "hu_HU" -> "Hungarian (Hungary)"
        "en_GB" -> "English (United Kingdom)"
        "de_DE" -> "German (Germany)"
        "es_ES" -> "Spanish (Spain)"
        "it_IT" -> "Italian (Italy)"
        "fr_FR" -> "French (France)"
        "ja_JP" -> "Japanese (Japan)"
        "ko_KR" -> "Korean (Korea)"
        "es_MX" -> "Spanish (Mexico)"
        "es_AR" -> "Spanish (Argentina)"
        "pt_BR" -> "Portuguese (Brazil)"
        "en_US" -> "English (United States)"
        "en_AU" -> "English (Australia)"
        "ru_RU" -> "Russian (Russia)"
        "tr_TR" -> "Turkish (Turkey)"
        "ms_MY" -> "Malay (Malaysia)"
        "en_PH" -> "English (Republic of the Philippines)"
        "en_SG" -> "English (Singapore)"
        "th_TH" -> "Thai (Thailand)"
        "vi_VN" -> "Vietnamese (Viet Nam)"
        "id_ID" -> "Indonesian (Indonesia)"
        "zh_MY" -> "Chinese (Malaysia)"
        "zh_CN" -> "Chinese (China)"
        "zh_TW" -> "Chinese (Taiwan)"
        else -> languageCode
    }
}

@Preview
@Composable
private fun LanguageDialogContentPreview() {
    LanguageDialogContent(
        selectedLanguage = "English",
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