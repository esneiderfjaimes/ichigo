package com.red.code015.database.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.red.code015.domain.Account
import com.red.code015.domain.League
import com.red.code015.domain.PlatformID

@Entity(tableName = "summoner_table")
data class SummonerEntity(
    @PrimaryKey
    @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "puu_id") val puuId: String,
    @ColumnInfo(name = "summoner_id") val id: String,
    @ColumnInfo(name = "summoner_platform") val platformID: PlatformID,
    @ColumnInfo(name = "summoner_name") val name: String,
    @ColumnInfo(name = "summoner_profile_icon_id") val profileIconId: Int,
    @ColumnInfo(name = "summoner_level") val level: Int,
    @Embedded val account: Account?,
    @ColumnInfo(name = "summoner_leagues") val leagues: List<League>,
    @ColumnInfo(name = "last_check_date") val lastCheckDate: Long,
)

@Entity(tableName = "masteries_table")
data class MasteriesEntity(
    @PrimaryKey
    @ColumnInfo(name = "summoner_id") val summonerId: String,
    @ColumnInfo(name = "summoner_platform") val platformID: PlatformID,
    @ColumnInfo(name = "last_check_date") val lastCheckDate: Long,
    @ColumnInfo(name = "masteries") val data: List<MasteryEntity>,
)

data class MasteryEntity(
    val championId: Int,
    val championLevel: Int,
    val championPoints: Long,
    val lastPlayTime: Long,
    val championPointsSinceLastLevel: Long,
    val championPointsUntilNextLevel: Long,
    val chestGranted: Boolean,
    val tokensEarned: Int,
)