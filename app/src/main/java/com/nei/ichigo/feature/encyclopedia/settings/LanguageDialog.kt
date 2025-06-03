package com.nei.ichigo.feature.encyclopedia.settings

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.IchigoDialogContent
import com.nei.ichigo.core.designsystem.component.IchigoTitleDialog
import com.nei.ichigo.core.designsystem.component.SelectListContent
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
            onDismiss = onDismiss
        )
    }
}

@Composable
fun LanguageDialogContent(
    selectedLanguage: String?,
    languages: List<String>,
    onLanguageSelected: (String?) -> Unit,
    onDismiss: () -> Unit = {}
) {
    IchigoDialogContent(
        onCloseRequest = onDismiss,
        title = { IchigoTitleDialog(text = stringResource(R.string.select_language)) },
    ) {
        SelectListContent(
            selectedItem = selectedLanguage,
            items = languages,
            itemLabelNull = { stringResource(R.string.automatic) },
            itemLabel = { languageCodeToString(it) },
            onSelectItem = onLanguageSelected
        )
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