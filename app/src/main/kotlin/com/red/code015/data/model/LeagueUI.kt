package com.red.code015.data.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.red.code015.R
import com.red.code015.data.model.StatsLeague.Companion.compareStatsLeague
import com.red.code015.domain.MiniSeries
import com.red.code015.domain.QueueType
import com.red.code015.domain.Tier

data class LeagueSummaryUI(
    val queueType: QueueType,
    val tier: Tier,
    val rank: String,
    val points: Long,
) : Comparable<LeagueSummaryUI> {

    override fun compareTo(other: LeagueSummaryUI): Int = compare(other)

    @Composable
    @ReadOnlyComposable
    fun summary(leaguePoints: Long?): AnnotatedString = buildAnnotatedString {
        AppendTierAndRank(tier, rank)
        withStyle(style = SpanStyle(color = Color.Gray)) { append(" | ") }
        withStyle(style = SpanStyle(color = when {
            points > leaguePoints ?: 0 -> MaterialTheme.colorScheme.primary
            points < leaguePoints ?: 0 -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurface
        })) {
            append("$points LP")
            if (leaguePoints != null && points - leaguePoints != 0L) {
                append(" ${points - leaguePoints} LP")
            }
        }
    }

    companion object {

        @Composable
        @ReadOnlyComposable
        fun List<LeagueSummaryUI>.summaryRelevant(leaguePoints: Long? = null) =
            minOrNull()?.summary(leaguePoints) ?: buildAnnotatedString { append("-") }

        private fun LeagueSummaryUI?.compare(other: LeagueSummaryUI?): Int {
            if (other == null && this == null) return 0
            if (other == null) return -1
            if (this == null) return 1

            return compareStatsLeague(
                StatsLeague(tier, rank, queueType, points),
                other.run { StatsLeague(tier, rank, queueType, points) }
            )
        }
    }
}

data class LeagueUI(
    val queueType: QueueType,
    val idResQueueType: Int,
    val tier: Tier,
    val rank: String,
    val points: Long,
    val wins: Int,
    val losses: Int,
    val hotStreak: Boolean,
    val color: Color,
    val idResRank: Int,
    val miniSeries: MiniSeries? = null,
) : Comparable<LeagueUI> {

    override fun compareTo(other: LeagueUI) = compare(other)

    @Composable
    @ReadOnlyComposable
    fun summary(): AnnotatedString = buildAnnotatedString { AppendTierAndRank(tier, rank) }

    companion object {
        private fun LeagueUI?.compare(other: LeagueUI?): Int {
            if (other == null && this == null) return 0
            if (other == null) return -1
            if (this == null) return 1

            return compareStatsLeague(
                StatsLeague(tier, rank, queueType, points),
                other.run { StatsLeague(tier, rank, queueType, points) }
            )
        }
    }
}

private data class StatsLeague(
    val tier: Tier,
    val rank: String,
    val queueType: QueueType,
    val points: Long,
) {
    fun compareTo(other: StatsLeague): Int {
        if (tier.ordinal > other.tier.ordinal) return -1
        if (tier.ordinal < other.tier.ordinal) return 1
        // Same tier

        if (rankToOrdinal(rank) > rankToOrdinal(other.rank)) return -1
        if (rankToOrdinal(rank) < rankToOrdinal(other.rank)) return 1
        // Same rake

        if (queueType.ordinal > other.queueType.ordinal) return -1
        if (queueType.ordinal < other.queueType.ordinal) return 1
        // Same queueType

        return when {
            points > other.points -> -1
            points < other.points -> 1
            else -> 0
        }
    }

    companion object {
        fun compareStatsLeague(statsLeague0: StatsLeague, statsLeague1: StatsLeague) =
            statsLeague0.compareTo(statsLeague1)

        private fun rankToOrdinal(rank: String) = when (rank) {
            "I" -> 0
            "II" -> 1
            "III" -> 2
            "IV" -> 3
            else -> -1
        }
    }
}

@Composable
@ReadOnlyComposable
private fun AnnotatedString.Builder.AppendTierAndRank(
    tier: Tier,
    rank: String,
) = append(when (tier) {
    Tier.Unknown -> "Unknown $rank"
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