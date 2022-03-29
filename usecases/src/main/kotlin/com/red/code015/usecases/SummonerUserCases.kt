package com.red.code015.usecases

import com.red.code015.data.SummonerRepository
import com.red.code015.domain.SummonerSummary
import io.reactivex.Single
import javax.inject.Inject

class SummonerByNameUserCase @Inject constructor(private val repository: SummonerRepository) {
    fun invoke(name: String): Single<SummonerSummary> = repository.getByName(name)
}
