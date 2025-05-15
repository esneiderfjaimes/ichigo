package com.nei.ichigo.core.data.repository

import android.content.Context
import com.nei.ichigo.core.data.R
import com.nei.ichigo.core.data.model.ChampionsPage
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

internal class OnlineChampionsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource
) : ChampionsRepository {
    override suspend fun getLanguages(): List<String> {
        return networkDataSource.getLanguages()
    }

    override suspend fun getVersions(): List<String> {
        return networkDataSource.getVersions()
    }

    private suspend fun getCurrentVersion(): String {
        val userVersionSelected: String? = null
        val versions = getVersions()
        return if (userVersionSelected != null && versions.contains(userVersionSelected)) {
            userVersionSelected
        } else {
            versions.first()
        }
    }

    private suspend fun getCurrentLang(): String {
        val userLangSelected: String? = null
        val languages = getLanguages()

        fun automaticSelection(): String {
            val langFromRes = context.getString(R.string.core_data_lang_code)
            return if (languages.contains(langFromRes)) {
                langFromRes
            } else {
                languages.first()
            }
        }

        return if (userLangSelected != null && languages.contains(userLangSelected)) {
            userLangSelected
        } else {
            automaticSelection()
        }
    }

    override suspend fun getChampionsPage(): ChampionsPage {
        val version = getCurrentVersion()
        val lang = getCurrentLang()
        return ChampionsPage(
            version = version,
            lang = lang,
            champions = networkDataSource.getChampions(version, lang)
        )
    }
}