package com.red.code015.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface SummonerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summonerEntity: SummonerEntity)

    @Query("SELECT * FROM summoner_table WHERE summoner_name = :name")
    fun byName(name: String): Maybe<SummonerEntity>

    @Query("SELECT last_check_date FROM summoner_table WHERE summoner_name = :name")
    fun lastCheckDateByName(name: String): Long?

}
