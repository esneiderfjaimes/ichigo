package com.red.code015.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SummonerEntity::class, MasteriesEntity::class], version = 2)
@TypeConverters(LeaguesConverter::class)
abstract class IchigoDatabase : RoomDatabase() {

    abstract fun summonerDao(): SummonerDao

    abstract fun masteriesDao(): MasteriesDao

    companion object {

        private const val DATABASE_NAME = "ichigo_database"

        @Synchronized
        fun getDatabase(context: Context): IchigoDatabase = Room.databaseBuilder(
            context,
            IchigoDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

}
