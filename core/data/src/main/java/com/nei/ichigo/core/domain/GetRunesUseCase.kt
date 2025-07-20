package com.nei.ichigo.core.domain

import android.content.Context
import com.nei.ichigo.core.data.model.Page
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.model.RuneBranch
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetRunesUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<Page<RuneBranch>>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    operator fun invoke() = flow

    override suspend fun fetchPage(version: String, lang: String) = kotlin.runCatching {
        Page(
            version = version,
            lang = lang,
            data = networkDataSource.getRunes(version, lang)
        )
    }
}