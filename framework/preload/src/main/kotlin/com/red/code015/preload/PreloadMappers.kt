package com.red.code015.preload

import android.content.Context
import com.red.code015.domain.ChampListItem
import com.red.code015.domain.Champion
import com.red.code015.preload.assets.*

fun Map<String, ChampJson>.toDomain(): Map<String, Champion> =
    map { it.key to it.value.toDomain() }.toMap()

fun ChampJson.toDomain() = Champion(
    version = version,
    id = id,
    key = key,
    name = name,
    title = title,
    image = image,
    blurb = blurb,
    info = info,
    tags = tags,
    partype = parType,
    stats = stats,
    skins = skins ?: emptyList(),
    lore = lore,
    allytips = allyTips ?: emptyList(),
    enemytips = enemyTips ?: emptyList(),
    spells = spells ?: emptyList(),
    passive = passive,
    recommended = recommended ?: emptyList()
)

fun Map<String, ChampSummaryJson>.toChampsList(applicationContext: Context): List<ChampListItem> =
    map { it.value.toChampsListItem(applicationContext) }

fun ChampSummaryJson.toChampsListItem(ctx: Context, forceImageFetch: Boolean = ForceImageFetch) =
    ChampListItem(
        id = id,
        key = key,
        name = name,
        image = image,
        tags = tags,
        bitmap = if (forceImageFetch) null
        else ctx.assetsToBitmap("$FolderChampsThumbnail/$id.webp")
    )
