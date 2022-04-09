package com.red.code015.database

import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.database.room.IchigoDatabase
import com.red.code015.domain.Summoner

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

    override suspend fun getLastCheckDateByName(name: String) = dao.lastCheckDateByName(name)

    override suspend fun summonerByName(name: String) = dao.byName(name).toDomain()

    // By Riot ID

    override suspend fun getLastCheckDateByRiotId(gameName: String, tagLine: String)
            : Long? = dao.lastCheckDateByRiotId(gameName, tagLine)

    override suspend fun summonerByRiotId(gameName: String, tagLine: String)
            : Summoner = dao.byRiotID(gameName, tagLine).toDomain()

    // endregion

}
