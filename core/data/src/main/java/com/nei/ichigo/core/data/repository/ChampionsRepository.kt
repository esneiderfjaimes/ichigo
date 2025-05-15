package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.data.model.ChampionsPage

interface ChampionsRepository {

    suspend fun getVersions(): List<String>

    suspend fun getLanguages(): List<String>

    suspend fun getChampionsPage(): ChampionsPage

}