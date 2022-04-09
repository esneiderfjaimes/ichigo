package com.red.code015.database.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.red.code015.domain.Account
import com.red.code015.domain.League

@Entity(tableName = "summoner_table")
data class SummonerEntity(
    @PrimaryKey
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "puu_id") val puuId: String,
    @ColumnInfo(name = "summoner_id") val id: String,
    @ColumnInfo(name = "summoner_name") val name: String,
    @ColumnInfo(name = "summoner_profile_icon_id") val profileIconId: Int,
    @ColumnInfo(name = "summoner_level") val level: Int,
    @Embedded val account: Account?,
    @ColumnInfo(name = "summoner_leagues") val leagues: List<League>,
    @ColumnInfo(name = "last_check_date") val lastCheckDate: Long,
)