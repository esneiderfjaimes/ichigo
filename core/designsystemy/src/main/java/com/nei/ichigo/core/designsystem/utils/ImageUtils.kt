package com.nei.ichigo.core.designsystem.utils

// https://ddragon.leagueoflegends.com/cdn/12.7.1/img/champion/Aatrox.png
fun getChampionImage(championName: String, version: String): String =
    /*
    "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/${championName}"
    */
    "https://opgg-static.akamaized.net/meta/images/lol/$version/champion/${championName}?image=c_crop,h_103,w_103,x_9,y_9/q_auto:good,f_webp,w_160,h_160&v=1510"

// https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg
fun getChampionSkinImage(championName: String, number: Int): String =
    "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${championName}_$number.jpg"

// https://ddragon.leagueoflegends.com/cdn/img/champion/loading/Aatrox_0.jpg
fun getChampionSkinLoadingImage(championName: String, number: Int): String =
    "https://ddragon.leagueoflegends.com/cdn/img/champion/loading/${championName}_$number.jpg"

fun getProfileIconImage(iconId: String, version: String): String =
    "https://ddragon.leagueoflegends.com/cdn/$version/img/profileicon/$iconId"

// https://ddragon.leagueoflegends.com/cdn/15.12.1/img/item/3110.png
fun getItemImage(iconId: String, version: String): String =
    "https://ddragon.leagueoflegends.com/cdn/$version/img/item/$iconId"

// https://ddragon.leagueoflegends.com/cdn/15.12.1/img/spell/SummonerFlash.png
fun getSpellImage(iconId: String, version: String): String =
    "https://ddragon.leagueoflegends.com/cdn/$version/img/spell/$iconId"