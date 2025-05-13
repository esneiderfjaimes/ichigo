package com.red.code015.utils

const val ANIMATIONS_ON = true
const val MAX_PROFILES = 10

object Coil {

    fun urlProfileIcon(profileIconId: Int) =
        "https://ddragon.leagueoflegends.com/cdn/12.6.1/img/profileicon/$profileIconId.png"

    fun sprite(sprite: String) =
        "https://ddragon.leagueoflegends.com/cdn/12.7.1/img/sprite/$sprite"

    fun champion(championImage: String) =
        "https://opgg-static.akamaized.net/images/lol/champion/$championImage"
    //  "http://ddragon.leagueoflegends.com/cdn/12.7.1/img/champion/$championImage"

    fun splash(championId: String, skinNum: Int = 0) =
        "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${championId}_$skinNum.jpg"

}