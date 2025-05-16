package com.nei.ichigo.core.domain

import android.content.Context
import com.nei.ichigo.core.data.R
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

abstract class PagerHelper<T>(
    private val context: Context,
    private val championsRepository: ChampionsRepository,
    private val ichigoPreferencesDataSource: IchigoPreferencesDataSource
) {
    operator fun invoke(): Flow<Result<T>> = ichigoPreferencesDataSource.userSettings
        .map {
            val lang = getCurrentLang(it.langSelected)
            val version = getCurrentVersion(it.versionSelected)
            version to lang
        }.distinctUntilChanged().map { (version, lang) ->
            fetchPage(version, lang)
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)

    protected abstract suspend fun fetchPage(version: String, lang: String): Result<T>

    private suspend fun getCurrentVersion(userVersionSelected: String?): String {
        val versions = championsRepository.getVersions()
        return if (userVersionSelected != null && versions.contains(userVersionSelected)) {
            userVersionSelected
        } else {
            versions.first()
        }
    }

    private suspend fun getCurrentLang(userLangSelected: String?): String {
        val languages = championsRepository.getLanguages()

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
}