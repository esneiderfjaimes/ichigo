package com.red.code015.database.redbox

import android.app.Application
import com.google.gson.Gson
import javax.inject.Inject

class Redbox @Inject constructor(app: Application, gson: Gson) {

    val encyclopediaChampion: EncyclopediaChampionRB by lazy { EncyclopediaChampionRB(app, gson) }

    val championsRotation: ChampionsRotationRB by lazy { ChampionsRotationRB(app, gson) }

}