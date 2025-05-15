package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.data.model.ChampionsPage
import kotlinx.coroutines.flow.Flow

interface ChampionsRepository {

    fun getChampionsPage(): Flow<Result<ChampionsPage>>

    suspend fun getVersions(): List<String>

    suspend fun getLanguages(): List<String>

}