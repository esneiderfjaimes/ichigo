package com.nei.ichigo.core.domain

import android.content.Context
import android.util.Log
import com.nei.ichigo.core.data.model.ProfileIconsPage
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.core.network.BuildConfig
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val FORCE_FETCH_ICONS = false

class GetProfileIconsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<ProfileIconsPage>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    override suspend fun fetchPage(version: String, lang: String) = kotlin.runCatching {
        val icons: List<ProfileIcon> = kotlin.runCatching {
            if (BuildConfig.DEBUG && FORCE_FETCH_ICONS) {
                throw IllegalStateException("Forced fetch")
            }

            TODO()
        }.getOrElse { throwable ->
            Log.e("OnlineChampions", "Error getting champions from database", throwable)
            networkDataSource.getProfileIcons(version, lang).also { icons ->
                // TODO save to database
            }
        }

        ProfileIconsPage(
            version = version,
            lang = lang,
            icons = icons
        )
    }
}