package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.data.model.ChampionsPage
import com.nei.ichigo.core.model.Champion

interface ChampionsRepository {

    suspend fun getVersions(): List<String>

    suspend fun getChampions(): List<Champion>

    suspend fun getChampionsPage(): ChampionsPage

}