package com.red.code015.data.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.red.code015.R
import com.red.code015.domain.Image
import com.red.code015.domain.Skin

data class MasteryUI(
    val championId: Int,
    val championLevel: Int,
    val championPoints: Long,
    val lastPlayTime: Long,
    val championPointsSinceLastLevel: Long,
    val championPointsUntilNextLevel: Long,
    val chestGranted: Boolean,
    val tokensEarned: Int,
    val champListItem: ChampListItemUI,
) {
    enum class SortBy(
        private val comparator: Comparator<MasteryUI>,
        val defaultIsReverse: Boolean = false,
    ) {
        Points(SortByPoints, true),
        Level(SortByLevel, true),
        Alpha(SortByAlpha),
        LastPlayTime(SortByLastPlayTime, true);

        fun inList(list: List<MasteryUI>, reverse: Boolean) =
            list.sortedWith(comparator, reverse)

    }

    object SortByLastPlayTime : Comparator<MasteryUI> {

        override fun compare(p0: MasteryUI?, p1: MasteryUI?): Int = p0.run {
            when {
                this == null && p1 == null -> 0
                p1 == null -> -1
                this == null -> 1
                this.lastPlayTime != p1.lastPlayTime -> this.lastPlayTime compareTo p1.lastPlayTime
                else -> 0
            }
        }
    }

    object SortByAlpha : Comparator<MasteryUI> {

        override fun compare(p0: MasteryUI?, p1: MasteryUI?): Int = p0.run {
            when {
                this == null && p1 == null -> 0
                p1 == null -> -1
                this == null -> 1
                this.champListItem.name != p1.champListItem.name -> this.champListItem.name compareTo p1.champListItem.name
                else -> 0
            }
        }
    }

    object SortByLevel : Comparator<MasteryUI> {

        override fun compare(p0: MasteryUI?, p1: MasteryUI?): Int = p0.run {
            when {
                this == null && p1 == null -> 0
                p1 == null -> -1
                this == null -> 1
                this.championLevel != p1.championLevel -> this.championLevel compareTo p1.championLevel
                this.championPoints != p1.championPoints -> this.championPoints compareTo p1.championPoints
                else -> 0
            }
        }
    }

    object SortByPoints : Comparator<MasteryUI> {

        override fun compare(p0: MasteryUI?, p1: MasteryUI?): Int = p0.run {
            when {
                this == null && p1 == null -> 0
                p1 == null -> -1
                this == null -> 1
                this.championPoints != p1.championPoints -> this.championPoints compareTo p1.championPoints
                this.championLevel != p1.championLevel -> this.championLevel compareTo p1.championLevel
                else -> 0
            }
        }
    }

    object SortByLevelAndAlpha : Comparator<MasteryUI> {

        override fun compare(p0: MasteryUI?, p1: MasteryUI?): Int = p0.run {
            when {
                this == null && p1 == null -> 0
                p1 == null -> -1
                this == null -> 1
                this.championLevel != p1.championLevel -> this.championLevel compareTo p1.championLevel
                this.champListItem.name != p1.champListItem.name -> this.champListItem.name compareTo p1.champListItem.name
                else -> 0
            }
        }
    }
}

fun <T> List<T>.sortedWith(comparator: Comparator<T>, reverse: Boolean)
        : List<T> = sortedWith(comparator).run {
    if (reverse) reversed() else this
}

fun List<MasteryUI>.sortByLevel() = sortedWith(MasteryUI.SortByLevelAndAlpha)

data class ChampListItemUI(
    val id: String,
    val key: String,
    val name: String,
    val skins: List<Skin>,
    val image: Image,
    val bitmap: Bitmap? = null,
)

fun MasteryUI.getMasteryItem() = when (championLevel) {
    7 -> R.mipmap.mastery_item7
    6 -> R.mipmap.mastery_item6
    5 -> R.mipmap.mastery_item5
    4 -> R.mipmap.mastery_item4
    3 -> R.mipmap.mastery_item3
    2 -> R.mipmap.mastery_item2
    1 -> R.mipmap.mastery_item1
    else -> null
}

fun MasteryUI.getMasteryToken(isIcon: Boolean = false) = when (championLevel) {
    6 -> if (isIcon) R.mipmap.token7 else R.mipmap.token_border7
    5 -> if (isIcon) R.mipmap.token6 else R.mipmap.token_border6
    else -> R.mipmap.token
}

fun MasteryUI.getMastery() = when (championLevel) {
    7 -> R.mipmap.mastery7
    6 -> R.mipmap.mastery6
    5 -> R.mipmap.mastery5
    4 -> R.mipmap.mastery4
    3 -> R.mipmap.mastery3
    2 -> R.mipmap.mastery2
    else -> R.mipmap.mastery1
}

fun MasteryUI.getColorMastery(isLightMode: Boolean = false) =
    if (isLightMode) when (championLevel) {
        7 -> Color(0xFF4FD8D3)
        6 -> Color(0xFFC52EBB)
        5 -> Color(0xFFAE3B41)
        else -> Color(0xFFB79246)
    } else when (championLevel) {
        7 -> Color(0xFF289891)
        6 -> Color(0xFF861E80)
        5 -> Color(0xFFAE3B41)
        else -> Color(0xFFB79246)
    }

enum class BgID {
    Default,
    Random,
    Lasted
}

fun List<MasteryUI>.getBackgroundId(random: Boolean = false, bgID: BgID = BgID.Lasted) =
    run { if (random) shuffled() else this }
        .first().champListItem.getBackgroundId(bgID)

fun ChampListItemUI.getBackgroundId(bgID: BgID)
        : String = id + "_" + when {
    bgID == BgID.Default || skins.isEmpty() -> "0" // default
    bgID == BgID.Lasted -> skins.last().num // lasted
    bgID == BgID.Random -> skins.shuffled().last().num // random
    else -> "0"
}