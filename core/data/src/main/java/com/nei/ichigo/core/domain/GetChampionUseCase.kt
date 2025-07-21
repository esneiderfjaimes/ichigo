package com.nei.ichigo.core.domain

import com.nei.ichigo.core.data.repository.PagerHelper
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import javax.inject.Inject

class GetChampionUseCase @Inject constructor(
    private val networkDataSource: IchigoNetworkDataSource,
    private val pagerHelper: PagerHelper
) {
    operator fun invoke(championKey: String) = pagerHelper.createPage { version, lang ->
        networkDataSource.getChampion(version, lang, championKey)
    }
}