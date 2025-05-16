package com.nei.ichigo.core.network

import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.model.ProfileIcon

interface IchigoNetworkDataSource {

    suspend fun getVersions(): Result<List<String>>

    suspend fun getLanguages(): Result<List<String>>

    suspend fun getChampions(version: String, lang: String): List<Champion>

    suspend fun getProfileIcons(version: String, lang: String): List<ProfileIcon>
}
