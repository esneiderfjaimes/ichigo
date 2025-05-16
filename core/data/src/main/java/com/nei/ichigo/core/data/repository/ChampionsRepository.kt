package com.nei.ichigo.core.data.repository

interface ChampionsRepository {

    suspend fun getVersions(): List<String>

    suspend fun getLanguages(): List<String>

}