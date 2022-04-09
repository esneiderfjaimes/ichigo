package com.red.code015.api.retrofit

import com.google.gson.annotations.SerializedName

data class SummonerResponseServer(
    @SerializedName("id") val id: String,
    @SerializedName("accountId") val accountId: String,
    @SerializedName("puuid") val puuId: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("revisionDate") val revisionDate: Long,
    @SerializedName("summonerLevel") val level: Int,
)

data class AccountResponseServer(
    @SerializedName("puuid") val puuid: String,
    @SerializedName("gameName") val gameName: String?,
    @SerializedName("tagLine") val tagLine: String?,
)

data class LeagueResponseServer(
    @SerializedName("leagueId") val leagueId: String,
    @SerializedName("queueType") val queueType: String,
    @SerializedName("tier") val tier: String?,
    @SerializedName("rank") val rank: String?,
    @SerializedName("summonerId") val summonerId: String,
    @SerializedName("summonerName") val summonerName: String,
    @SerializedName("leaguePoints") val points: Long,
    @SerializedName("wins") val wins: Int,
    @SerializedName("losses") val losses: Int,
    @SerializedName("veteran") val veteran: Boolean,
    @SerializedName("inactive") val inactive: Boolean,
    @SerializedName("freshBlood") val freshBlood: Boolean,
    @SerializedName("hotStreak") val hotStreak: Boolean,
)