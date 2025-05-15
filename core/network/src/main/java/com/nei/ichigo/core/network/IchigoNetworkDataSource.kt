package com.nei.ichigo.core.network

import com.nei.ichigo.core.model.Champion

interface IchigoNetworkDataSource {

    suspend fun getVersions(): List<String>

    suspend fun getLanguages(): List<String>

    suspend fun getChampions(version: String, lang: String): List<Champion>
}
