package com.red.code015.database

import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RedboxDataSource
import com.red.code015.database.redbox.Redbox
import com.red.code015.database.room.IchigoDatabase
import com.red.code015.domain.*
import java.util.*

class SummonerRoomDataSource(
    database: IchigoDatabase,
) : LocalSummonerDataSource {

    private val dao by lazy { database.summonerDao() }

    // region Insert and update

    override suspend fun insertSummoner(summoner: Summoner) {
        dao.insert(summoner.toEntity())
    }

    // endregion
    // region Read

    // By Puu ID

    override suspend fun getLastCheckDateByPuuID(puuID: String) = dao.lastCheckDateByPuuID(puuID)

    override suspend fun summonerByPuuID(puuID: String) = dao.byPuuID(puuID).toDomain()

    // By Name

    override suspend fun getLastCheckDateByName(platformID: PlatformID, name: String) =
        dao.lastCheckDateByName(platformID, name)

    override suspend fun summonerByName(platformID: PlatformID, name: String) =
        dao.byName(platformID, name).toDomain()

    // By Riot ID

    override suspend fun getLastCheckDateByRiotId(gameName: String, tagLine: String)
            : Long? = dao.lastCheckDateByRiotId(gameName, tagLine)

    override suspend fun summonerByRiotId(gameName: String, tagLine: String)
            : Summoner = dao.byRiotID(gameName, tagLine).toDomain()

    // endregion

}

class DragonRedboxDataSource(
    private val redbox: Redbox,
) : RedboxDataSource {

    private val daoEncyclopediaChampion by lazy { redbox.encyclopediaChampion }
    private val daoChampionsRotation by lazy { redbox.championsRotation }

    override suspend fun insetEncyclopediaChampion(
        encyclopediaChampion: EncyclopediaChampion,
        lang: String,
    ) {
        daoEncyclopediaChampion.insert(encyclopediaChampion.copy(dataSource = updateDS()), lang)
    }

    override suspend fun readEncyclopediaChampion(lang: String)
            : EncyclopediaChampion? = daoEncyclopediaChampion.read(lang)

    override suspend fun insetChampionsRotation(championsRotation: ChampionsRotation) {
        daoChampionsRotation.insert(championsRotation.copy(dataSource = updateDS()))
    }

    override suspend fun readChampionsRotation()
            : ChampionsRotation? = daoChampionsRotation.read()

    private fun updateDS() = DataSource(dataSources = DataSources.DATABASE, time = Date().time)
}
