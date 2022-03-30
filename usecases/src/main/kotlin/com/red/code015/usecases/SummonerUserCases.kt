package com.red.code015.usecases

import com.red.code015.data.SummonerRepository
import com.red.code015.domain.SummonerSummary
import io.reactivex.Maybe
import javax.inject.Inject

class SummonerByNameUserCase @Inject constructor(private val repository: SummonerRepository) {
    suspend fun invoke(name: String): Maybe<SummonerSummary> = repository.getSummonerByName(name)
}
