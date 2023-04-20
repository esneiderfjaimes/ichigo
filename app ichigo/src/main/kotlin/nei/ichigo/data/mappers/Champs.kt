package nei.ichigo.data.mappers

import ichigo.core.api.retrofit.responses.ChampionRS
import ichigo.core.api.retrofit.responses.ImageRS
import nei.ichigo.data.modeles.ChampListItem
import nei.ichigo.data.modeles.Image

fun ChampionRS.toChampListItem() = ChampListItem(
    id = id!!,
    name = name!!,
    image = image!!.toImage(),
)

fun ImageRS.toImage() = Image(
    null,
    full!!,
    sprite!!,
    group!!,
    x!!,
    y!!,
    w!!,
    h!!,
)