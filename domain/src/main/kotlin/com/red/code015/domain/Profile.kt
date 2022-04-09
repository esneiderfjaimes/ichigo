package com.red.code015.domain

data class Profile(
    val platformID: PlatformID,
    val puuID: String, // Global
    val summonerID: String, // Unique per region
    val accountID: String, // Unique per region
    val name: String,
    val profileIconID: Int,
)