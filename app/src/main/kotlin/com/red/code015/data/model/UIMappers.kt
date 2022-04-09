package com.red.code015.data.model

import com.red.code015.domain.League
import com.red.code015.domain.Profile
import com.red.code015.domain.Summoner
import com.red.code015.domain.Tier

fun Summoner.toUI() = SummonerUI(name, profileIconId, level, account, leagues.toUI())

fun List<League>.toUI() = map { it.toUI() }

fun League.toUI() = LeagueUI(queueType, tier ?: Tier.Unknown, rank ?: "", points)

fun Profile.requiresUpdate(summonerUI: SummonerUI) =
    name != summonerUI.name || profileIconID != summonerUI.profileIconId

fun Profile.copyUpdates(summonerUI: SummonerUI) = copy(
    name = summonerUI.name,
    profileIconID = summonerUI.profileIconId
)