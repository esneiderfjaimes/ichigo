package com.red.code015.usecases

import com.red.code015.data.repositories.ProfileRepository
import com.red.code015.data.repositories.SummonerDragonRepository
import com.red.code015.data.repositories.SummonerRepository
import com.red.code015.domain.PlatformID


class ProfileByRiotIDUserCase constructor(private val repository: ProfileRepository) {
    fun invoke(gameName: String, tagline: String) = repository.byRiotID(gameName, tagline)
}

class ProfileBySummonerNameUserCase constructor(private val repository: ProfileRepository) {
    fun invoke(name: String) = repository.bySummonerName(name)
}

class SummonerByPuuIDUserCase constructor(private val repository: SummonerRepository) {
    fun invoke(id: String, forceFetch: Boolean = false) = repository.summonerByPuuID(id, forceFetch)
}

class SummonerBySummonerNameUserCase constructor(private val repository: SummonerRepository) {
    fun invoke(platformID: PlatformID, name: String, forceFetch: Boolean = false) =
        repository.summonerByName(platformID, name, forceFetch)
}

class MasteriesUserCase constructor(private val repository: SummonerDragonRepository) {
    fun invoke(
        platformID: PlatformID,
        summonerId: String,
        limit: Int = 0,
        forceFetch: Boolean = false,
    ) = repository.championMasteries(platformID, summonerId, limit)
}
