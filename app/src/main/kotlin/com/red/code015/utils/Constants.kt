package com.red.code015.utils

object Coil {

    fun urlProfileIcon(profileIconId: Int) =
        "https://ddragon.leagueoflegends.com/cdn/12.6.1/img/profileicon/$profileIconId.png"

    fun sprite(sprite: String) =
        "https://ddragon.leagueoflegends.com/cdn/12.7.1/img/sprite/$sprite"

    fun champion(championImage: String) =
        "https://opgg-static.akamaized.net/images/lol/champion/$championImage"
    //  "http://ddragon.leagueoflegends.com/cdn/12.7.1/img/champion/$championImage"

}