package com.red.code015.data.model

import com.red.code015.domain.Profile

fun Profile.requiresUpdate(summonerUI: SummonerSummaryUI) =
    name != summonerUI.name || profileIconID != summonerUI.profileIconId

fun Profile.copyUpdates(summonerUI: SummonerSummaryUI) = copy(
    name = summonerUI.name,
    profileIconID = summonerUI.profileIconId
)