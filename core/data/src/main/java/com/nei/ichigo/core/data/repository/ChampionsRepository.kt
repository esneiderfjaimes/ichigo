package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.model.Champion

interface ChampionsRepository {

    suspend fun getChampions(): List<Champion>

}