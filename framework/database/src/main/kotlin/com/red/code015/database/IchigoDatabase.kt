package com.red.code015.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SummonerEntity::class], version = 1)
abstract class IchigoDatabase : RoomDatabase() {

    abstract fun summonerDao(): SummonerDao

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
