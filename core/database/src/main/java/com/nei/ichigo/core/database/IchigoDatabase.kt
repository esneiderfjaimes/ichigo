package com.nei.ichigo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nei.ichigo.core.database.dao.ChampionDao
import com.nei.ichigo.core.database.dao.ProfileIconDao
import com.nei.ichigo.core.database.model.ChampionEntity
import com.nei.ichigo.core.database.model.ProfileIconEntity
import com.nei.ichigo.core.database.utils.Converters

@Database(
    entities = [
        ChampionEntity::class,
        ProfileIconEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
internal abstract class IchigoDatabase : RoomDatabase() {
    abstract fun championDao(): ChampionDao
    abstract fun profileIconDao(): ProfileIconDao
}