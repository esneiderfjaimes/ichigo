package com.nei.ichigo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nei.ichigo.core.database.model.ChampionEntity

@Dao
interface ChampionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChampions(champions: List<ChampionEntity>)

    @Query(
        """
        SELECT * FROM champion
        WHERE version = :version AND lang = :lang
        """
    )
    suspend fun getChampionsByVersionAndLang(
        version: String,
        lang: String
    ): List<ChampionEntity>
}

