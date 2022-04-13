package com.red.code015.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.red.code015.domain.PlatformID

@Dao
interface SummonerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(summonerEntity: SummonerEntity)

    @Query("SELECT last_check_date FROM summoner_table WHERE summoner_id = :puuid")
    suspend fun lastCheckDateByPuuID(puuid: String): Long?

    @Query("SELECT last_check_date FROM summoner_table " +
            "WHERE summoner_platform = :platformID AND upper(summoner_name) = upper(:name)")
    suspend fun lastCheckDateByName(platformID: PlatformID, name: String): Long?

    @Query("SELECT last_check_date FROM summoner_table WHERE gameName = :gameName AND tagLine = :tagLine")
    suspend fun lastCheckDateByRiotId(gameName: String, tagLine: String): Long?

    @Query("SELECT * FROM summoner_table WHERE summoner_id = :puuid")
    suspend fun byPuuID(puuid: String): SummonerEntity

    @Query("SELECT * FROM summoner_table WHERE summoner_platform = :platformID AND upper(summoner_name) = upper(:name)")
    suspend fun byName(platformID: PlatformID, name: String): SummonerEntity

    @Query("SELECT * FROM summoner_table WHERE gameName = :gameName AND tagLine = :tagLine")
    suspend fun byRiotID(gameName: String, tagLine: String): SummonerEntity

}
