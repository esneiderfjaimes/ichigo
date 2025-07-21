package com.nei.ichigo.core.network

import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.model.ChampionDetail
import com.nei.ichigo.core.model.Item
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.core.model.RuneBranch
import com.nei.ichigo.core.model.Spell

interface IchigoNetworkDataSource {

    suspend fun getVersions(): List<String>

    suspend fun getLanguages(): List<String>

    suspend fun getChampions(version: String, lang: String): List<Champion>

    suspend fun getChampion(version: String, lang: String, champKey: String): ChampionDetail

    suspend fun getProfileIcons(version: String, lang: String): List<ProfileIcon>

    suspend fun getItems(version: String, lang: String): List<Item>

    suspend fun getSummonerSpells(version: String, lang: String): List<Spell>

    suspend fun getRunes(version: String, lang: String): List<RuneBranch>
}
