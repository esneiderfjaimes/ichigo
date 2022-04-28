package com.red.code015.database.redbox

import android.app.Application
import com.google.gson.Gson
import com.red.code015.database.redbox.core.BaseRB
import com.red.code015.domain.ChampionsRotation
import com.red.code015.domain.EncyclopediaChampion
import javax.inject.Inject

class ChampionsRotationRB @Inject constructor(app: Application, gson: Gson) :
    BaseRB<ChampionsRotation>(
        cls = ChampionsRotation::class.java,
        nameFile = "champions_rotation",
        app = app,
        gson = gson
    )

class EncyclopediaChampionRB @Inject constructor(app: Application, gson: Gson) :
    BaseRB<EncyclopediaChampion>(
        cls = EncyclopediaChampion::class.java,
        nameFile = "encyclopedia_champion",
        app = app,
        gson = gson
    )
