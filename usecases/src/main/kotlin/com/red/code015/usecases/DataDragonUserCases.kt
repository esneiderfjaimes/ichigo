package com.red.code015.usecases

import com.red.code015.data.repositories.DataDragonRepository
import javax.inject.Inject

class EncyclopediaChampionUserCase @Inject constructor(private val repository: DataDragonRepository) {
    fun invoke() = repository.encyclopediaChampion()
}
