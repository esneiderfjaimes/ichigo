package com.red.code015.utils

object SettingsPref {
    const val NAME = "settings"
    const val KEY_SUMMONER = "user's summoner name"
}

object Coil {
    fun urlProfileIcon(profileIconId: Int) =
        "https://ddragon.leagueoflegends.com/cdn/12.6.1/img/profileicon/$profileIconId.png"
}