package com.nei.ichigo.core.data.repository

import android.content.Context
import com.nei.ichigo.core.data.R
import com.nei.ichigo.core.data.model.Config
import com.nei.ichigo.core.data.model.ConfigValue
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ichigoPreferencesDataSource: IchigoPreferencesDataSource,
    private val dragonRepository: DDragonRepository,
) : AppConfigRepository {

    override val config: Flow<Result<Config>>
        get() = combine(
            ichigoPreferencesDataSource.userSettings,
            dragonRepository.metaData
        ) { userSettings, resultMetaData ->
            runCatching {
                val metaData = resultMetaData.getOrThrow()
                val lang = getCurrentLang(userSettings.langSelected, metaData.languages)
                val version = getCurrentVersion(userSettings.versionSelected, metaData.versions)
                Config(version, lang)
            }
        }.distinctUntilChanged()


    private fun getCurrentVersion(
        userVersionSelected: String?,
        versions: List<String>
    ): ConfigValue {
        return if (userVersionSelected != null && versions.contains(userVersionSelected)) {
            ConfigValue.SelectedByUser(userVersionSelected)
        } else {
            ConfigValue.AutomaticSelection(versions.first())
        }
    }

    private fun getCurrentLang(userLangSelected: String?, languages: List<String>): ConfigValue {
        if (userLangSelected != null && languages.contains(userLangSelected)) {
            return ConfigValue.SelectedByUser(userLangSelected)
        }

        // Fallback
        val langFromRes = context.getString(R.string.core_data_lang_code)
        val automaticSelection = if (languages.contains(langFromRes)) {
            langFromRes
        } else {
            languages.first()
        }
        return ConfigValue.AutomaticSelection(automaticSelection)
    }
}