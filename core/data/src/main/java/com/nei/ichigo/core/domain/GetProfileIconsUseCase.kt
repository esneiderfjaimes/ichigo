package com.nei.ichigo.core.domain

import com.nei.ichigo.core.data.model.asEntity
import com.nei.ichigo.core.data.model.asExternalModel
import com.nei.ichigo.core.data.repository.PagerHelper
import com.nei.ichigo.core.database.dao.ProfileIconDao
import com.nei.ichigo.core.database.model.ProfileIconEntity
import com.nei.ichigo.core.network.BuildConfig
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import javax.inject.Inject

private const val FORCE_FETCH_ICONS = true

class GetProfileIconsUseCase @Inject constructor(
    private val networkDataSource: IchigoNetworkDataSource,
    private val profileIconDao: ProfileIconDao,
    private val pagerHelper: PagerHelper
) {
    operator fun invoke() = pagerHelper.createListPage { version, lang ->
        val count = profileIconDao.countByVersionAndLang(version, lang)
        if (count <= 0 || (BuildConfig.DEBUG && FORCE_FETCH_ICONS)) {
            val allIcons = networkDataSource.getProfileIcons(version, lang)
            val entities = allIcons.map { it.asEntity(version, lang) }
            profileIconDao.insertAll(entities)
        }

        profileIconDao.getProfileIcons(version, lang)
            .sortedByDescending { it.code.toInt() }
            .map(ProfileIconEntity::asExternalModel)
    }
}