package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.model.Image

class FakeChampionsRepository : ChampionsRepository {
    override suspend fun getChampions(): List<Champion> {
        return listOf(
            Champion(
                version = "15.9.1",
                id = "Aatrox",
                key = "266",
                name = "Aatrox",
                title = "the Darkin Blade",
                blurb = "Once honored defenders of Shurima against the Void, Aatrox and his brethren would eventually become an even greater threat to Runeterra, and were defeated only by cunning mortal sorcery. But after centuries of imprisonment, Aatrox was the first to find...",
                image = Image(
                    full = "Aatrox.png",
                    sprite = "champion0.png",
                    group = "champion",
                    x = 0,
                    y = 0,
                    w = 48,
                    h = 48
                ),
                tags = listOf("Fighter"),
                partype = "Blood Well",
            )
        )
    }
}