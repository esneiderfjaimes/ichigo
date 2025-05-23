package com.nei.ichigo.core.domain

import android.content.Context
import com.nei.ichigo.core.data.model.ProfileIconsPage
import com.nei.ichigo.core.data.model.asEntity
import com.nei.ichigo.core.data.model.asExternalModel
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.database.dao.ProfileIconDao
import com.nei.ichigo.core.database.model.ProfileIconEntity
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.network.BuildConfig
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val FORCE_FETCH_ICONS = false

class GetProfileIconsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    private val profileIconDao: ProfileIconDao,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<ProfileIconsPage>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    operator fun invoke() = flow

    override suspend fun fetchPage(version: String, lang: String) = kotlin.runCatching {
        val count = profileIconDao.countByVersionAndLang(version, lang)
        if (count <= 0 || (BuildConfig.DEBUG && FORCE_FETCH_ICONS)) {
            val allIcons = networkDataSource.getProfileIcons(version, lang)
            val entities = allIcons
                .sortedByDescending { it.id.toInt() }
                .map { it.asEntity(version, lang) }
            profileIconDao.insertAll(entities)
        }

        val profileIcons = profileIconDao.getProfileIcons(version, lang)

        ProfileIconsPage(
            version = version,
            lang = lang,
            icons = profileIcons.map(ProfileIconEntity::asExternalModel),
        )
    }
}