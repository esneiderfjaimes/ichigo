package com.red.code015.data.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.red.code015.R
import com.red.code015.R.mipmap.*
import com.red.code015.domain.League
import com.red.code015.domain.QueueType
import com.red.code015.domain.Tier

data class LeagueUI2(
    val queueType: QueueType,
    val idResQueueType: Int,
    val tier: Tier?,
    val rank: String?,
    val points: Long,
    val wins: Int,
    val losses: Int,
    val veteran: Boolean,
    val inactive: Boolean,
    val freshBlood: Boolean,
    val hotStreak: Boolean,
    val color: Color,
    val idResRank: Int,
)

fun League.toUI2() = LeagueUI2(
    queueType = queueType,
    idResQueueType = when (queueType) {
        QueueType.Unknown -> R.string.queue_type_unknown
        QueueType.RankedFlex -> R.string.queue_type_ranked_flex
        QueueType.RankedSolo -> R.string.queue_type_ranked_solo
    },
    tier = tier,
    rank = rank,
    points = points,
    wins = wins,
    losses = losses,
    veteran = veteran,
    inactive = inactive,
    freshBlood = freshBlood,
    hotStreak = hotStreak,
    color = when (tier) {
        Tier.Iron -> Color(0xFF817678)
        Tier.Bronze -> Color(0xFF9F6347)
        Tier.Silver -> Color(0xFF809890)
        Tier.Gold -> Color(0xFFCD8837)
        Tier.Platinum -> Color(0xFF4E9996)
        Tier.Diamond -> Color(0xFF576BCE)
        Tier.Master -> Color(0xFF9D48E0)
        Tier.GrandMaster -> Color(0xFFD94444)
        Tier.Challenger -> Color(0xFFF4C874)
        else -> Color.Black
    }, idResRank = when (tier) {
        Tier.Iron -> tier0_iron
        Tier.Bronze -> tier2_bronze
        Tier.Silver -> tier1_silver
        Tier.Gold -> tier3_gold
        Tier.Platinum -> tier4_platinum
        Tier.Diamond -> tier5_diamond
        Tier.Master -> tier6_master
        Tier.GrandMaster -> tier7_grandmaster
        Tier.Challenger -> tier8_challenger
        else -> 0
    })

@Composable
@ReadOnlyComposable
fun LeagueUI2.summary(): AnnotatedString = buildAnnotatedString {
    if (tier == null && rank == null) return@buildAnnotatedString
    val rank = this@summary.rank!!
    val tier = this@summary.tier!!

    append(when (tier) {
        Tier.Unknown -> "$queueType $rank"
        Tier.Iron -> stringResource(R.string.tier_iron, rank)
        Tier.Bronze -> stringResource(R.string.tier_bronze, rank)
        Tier.Silver -> stringResource(R.string.tier_silver, rank)
        Tier.Gold -> stringResource(R.string.tier_gold, rank)
        Tier.Platinum -> stringResource(R.string.tier_platinum, rank)
        Tier.Diamond -> stringResource(R.string.tier_diamond, rank)
        Tier.Master -> stringResource(R.string.tier_master)
        Tier.GrandMaster -> stringResource(R.string.tier_grand_master)
        Tier.Challenger -> stringResource(R.string.tier_challenger)
    })
}