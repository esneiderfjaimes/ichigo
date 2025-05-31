package com.nei.ichigo.core.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nei.ichigo.core.designsystem.R

private val languageCodeMap = mapOf(
    "ar_AE" to R.string.core_designsystemy_lang_ar_ae,
    "cs_CZ" to R.string.core_designsystemy_lang_cs_cz,
    "el_GR" to R.string.core_designsystemy_lang_el_gr,
    "pl_PL" to R.string.core_designsystemy_lang_pl_pl,
    "ro_RO" to R.string.core_designsystemy_lang_ro_ro,
    "hu_HU" to R.string.core_designsystemy_lang_hu_hu,
    "en_GB" to R.string.core_designsystemy_lang_en_gb,
    "de_DE" to R.string.core_designsystemy_lang_de_de,
    "es_ES" to R.string.core_designsystemy_lang_es_es,
    "it_IT" to R.string.core_designsystemy_lang_it_it,
    "fr_FR" to R.string.core_designsystemy_lang_fr_fr,
    "ja_JP" to R.string.core_designsystemy_lang_ja_jp,
    "ko_KR" to R.string.core_designsystemy_lang_ko_kr,
    "es_MX" to R.string.core_designsystemy_lang_es_mx,
    "es_AR" to R.string.core_designsystemy_lang_es_ar,
    "pt_BR" to R.string.core_designsystemy_lang_pt_br,
    "en_US" to R.string.core_designsystemy_lang_en_us,
    "en_AU" to R.string.core_designsystemy_lang_en_au,
    "ru_RU" to R.string.core_designsystemy_lang_ru_ru,
    "tr_TR" to R.string.core_designsystemy_lang_tr_tr,
    "ms_MY" to R.string.core_designsystemy_lang_ms_my,
    "en_PH" to R.string.core_designsystemy_lang_en_ph,
    "en_SG" to R.string.core_designsystemy_lang_en_sg,
    "th_TH" to R.string.core_designsystemy_lang_th_th,
    "vi_VN" to R.string.core_designsystemy_lang_vi_vn,
    "id_ID" to R.string.core_designsystemy_lang_id_id,
    "zh_MY" to R.string.core_designsystemy_lang_zh_my,
    "zh_CN" to R.string.core_designsystemy_lang_zh_cn,
    "zh_TW" to R.string.core_designsystemy_lang_zh_tw,
)

@Composable
fun languageCodeToString(languageCode: String): String {
    return languageCodeMap[languageCode]?.let { stringResource(it) } ?: languageCode
}

private val rolesMap: Map<String, Int> = mapOf(
    "assassin" to R.string.core_designsystemy_role_assassin,
    "fighter" to R.string.core_designsystemy_role_fighter,
    "mage" to R.string.core_designsystemy_role_mage,
    "marksman" to R.string.core_designsystemy_role_marksman,
    "support" to R.string.core_designsystemy_role_support,
    "tank" to R.string.core_designsystemy_role_tank,
)

@Composable
fun roleToString(role: String): String {
    return rolesMap[role.lowercase()]?.let { stringResource(it) } ?: role
}