package com.nei.ichigo.core.designsystem.utils

fun getChampionImage(championName: String, version: String): String =
    /*
    "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/${championName}"
    */
    "https://opgg-static.akamaized.net/meta/images/lol/$version/champion/${championName}?image=c_crop,h_103,w_103,x_9,y_9/q_auto:good,f_webp,w_160,h_160&v=1510"

fun getProfileIconImage(iconId: String, version: String): String =
    "https://ddragon.leagueoflegends.com/cdn/$version/img/profileicon/$iconId"