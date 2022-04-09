package com.red.code015.data.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.red.code015.R
import com.red.code015.domain.Account
import com.red.code015.domain.QueueType
import com.red.code015.domain.Tier

data class SummonerUI(
    val name: String,
    val profileIconId: Int,
    val level: Int,
    val account: Account?,
    var leagues: List<LeagueUI> = emptyList(),
)

data class LeagueUI(
    val queueType: QueueType,
    val tier: Tier,
    val rank: String,
    val points: Long,
) {
    @Composable
    @ReadOnlyComposable
    fun summary(): AnnotatedString = buildAnnotatedString {
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
        withStyle(style = SpanStyle(color = Color.Gray)) {
            append(" | ")
        }
        append("$points LP")
    }

    companion object {
        @Composable
        @ReadOnlyComposable
        fun List<LeagueUI>.summary() = relevant()?.summary() ?: buildAnnotatedString { append("-") }

        private fun List<LeagueUI>.relevant(): LeagueUI? {
            if (isEmpty()) return null
            if (size == 1) return get(0)
            var leagueUI: LeagueUI? = null
            forEach { if (it.compareTo(leagueUI)) leagueUI = it }
            return leagueUI
        }

        private fun LeagueUI.compareTo(leagueUI: LeagueUI?): Boolean {
            if (leagueUI == null) return true
            if (tier.ordinal > leagueUI.tier.ordinal) return true
            if (tier.ordinal < leagueUI.tier.ordinal) return false
            if (rankToOrdinal(rank) > rankToOrdinal(leagueUI.rank)) return true
            if (rankToOrdinal(rank) < rankToOrdinal(leagueUI.rank)) return false
            if (queueType.ordinal > leagueUI.queueType.ordinal) return true
            if (queueType.ordinal < leagueUI.queueType.ordinal) return false
            return points > leagueUI.points
        }

        private fun rankToOrdinal(rank: String) = when (rank) {
            "I" -> 0
            "II" -> 1
            "III" -> 2
            "IV" -> 3
            else -> -1
        }
    }
}

