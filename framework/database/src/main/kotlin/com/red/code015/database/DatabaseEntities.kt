package com.red.code015.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summoner_table")
data class SummonerEntity(
    @PrimaryKey
    @ColumnInfo(name = "summoner_id") val id: String,
    @ColumnInfo(name = "summoner_account_id") val accountId: String,
    @ColumnInfo(name = "summoner_puu_id") val puuId: String,
    @ColumnInfo(name = "summoner_name") val name: String,
    @ColumnInfo(name = "summoner_profile_icon_id") val profileIconId: Int,
    @ColumnInfo(name = "summoner_revision_date") val revisionDate: Long,
    @ColumnInfo(name = "summoner_level") val level: Int,
    @ColumnInfo(name = "last_check_date") val lastCheckDate: Long
)