package com.red.code015.database

import com.red.code015.data.LocalMasteriesDataSource
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RedboxDataSource
import com.red.code015.database.redbox.Redbox
import com.red.code015.database.room.IchigoDatabase
import com.red.code015.domain.*
import java.util.*

class SummonerRoomDataSource(
    database: IchigoDatabase,
) : LocalSummonerDataSource {

    init {
        print("here")
    }

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

class MasteriesRoomDataSource(
    database: IchigoDatabase,
) : LocalMasteriesDataSource {

    private val dao by lazy { database.masteriesDao() }

    override suspend fun insertSummoner(masteries: Masteries) {
        dao.insert(masteries.toEntity())
    }

    override suspend fun getLastCheckDate(platformID: PlatformID, summonerID: String): Long? =
        dao.lastCheckDateBySummonerId(platformID, summonerID)

    override suspend fun masteries(platformID: PlatformID, summonerID: String)
            : Masteries = dao.bySummonerId(platformID, summonerID).toDomain()

}

class DragonRedboxDataSource(
    private val redbox: Redbox,
) : RedboxDataSource {

    private val daoEncyclopediaChampion by lazy { redbox.encyclopediaChampion }
    private val daoChampionsRotation by lazy { redbox.championsRotation }
    private val daoChampion by lazy { redbox.champion }

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

    override suspend fun insertChampion(
        champion: Champion,
        suffix: String,
        vararg prefixes: String,
    ) {
        daoChampion.insert(champion, suffix, *prefixes)
    }

    override suspend fun readChampion(suffix: String, vararg prefixes: String)
            : Champion? = daoChampion.read(suffix, *prefixes)

    private fun updateDS() = DataSource(dataSources = DataSources.DATABASE, time = Date().time)
}
