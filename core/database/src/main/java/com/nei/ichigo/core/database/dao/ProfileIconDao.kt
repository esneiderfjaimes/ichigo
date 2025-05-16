package com.nei.ichigo.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nei.ichigo.core.database.model.ProfileIconEntity

@Dao
interface ProfileIconDao {

    @Query("SELECT * FROM profile_icons WHERE version = :version AND lang = :lang ORDER BY id ASC")
    fun getProfileIcons(version: String, lang: String): List<ProfileIconEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(icons: List<ProfileIconEntity>)

    @Query("SELECT COUNT(*) FROM profile_icons WHERE version = :version AND lang = :lang")
    suspend fun countByVersionAndLang(version: String, lang: String): Int

}