package com.nei.ichigo.core.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nei.ichigo.R

private val languageCodeMap = mapOf(
    "ar_AE" to R.string.lang_ar_ae,
    "cs_CZ" to R.string.lang_cs_cz,
    "el_GR" to R.string.lang_el_gr,
    "pl_PL" to R.string.lang_pl_pl,
    "ro_RO" to R.string.lang_ro_ro,
    "hu_HU" to R.string.lang_hu_hu,
    "en_GB" to R.string.lang_en_gb,
    "de_DE" to R.string.lang_de_de,
    "es_ES" to R.string.lang_es_es,
    "it_IT" to R.string.lang_it_it,
    "fr_FR" to R.string.lang_fr_fr,
    "ja_JP" to R.string.lang_ja_jp,
    "ko_KR" to R.string.lang_ko_kr,
    "es_MX" to R.string.lang_es_mx,
    "es_AR" to R.string.lang_es_ar,
    "pt_BR" to R.string.lang_pt_br,
    "en_US" to R.string.lang_en_us,
    "en_AU" to R.string.lang_en_au,
    "ru_RU" to R.string.lang_ru_ru,
    "tr_TR" to R.string.lang_tr_tr,
    "ms_MY" to R.string.lang_ms_my,
    "en_PH" to R.string.lang_en_ph,
    "en_SG" to R.string.lang_en_sg,
    "th_TH" to R.string.lang_th_th,
    "vi_VN" to R.string.lang_vi_vn,
    "id_ID" to R.string.lang_id_id,
    "zh_MY" to R.string.lang_zh_my,
    "zh_CN" to R.string.lang_zh_cn,
    "zh_TW" to R.string.lang_zh_tw,
)

@Composable
fun languageCodeToString(languageCode: String): String {
    return languageCodeMap[languageCode]?.let { stringResource(it) } ?: languageCode
}

private val rolesMap: Map<String, Int> = mapOf(
    "assassin" to R.string.role_assassin,
    "fighter" to R.string.role_fighter,
    "mage" to R.string.role_mage,
    "marksman" to R.string.role_marksman,
    "support" to R.string.role_support,
    "tank" to R.string.role_tank,
)

@Composable
fun roleToString(role: String): String {
    return rolesMap[role.lowercase()]?.let { stringResource(it) } ?: role
}