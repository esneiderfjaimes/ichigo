package com.red.code015.usecases

import com.red.code015.data.repositories.DataDragonRepository
import javax.inject.Inject

class LatestVersionUseCase @Inject constructor(private val repository: DataDragonRepository) {
    suspend fun invoke() = repository.lastVersion()
}

class EncyclopediaChampionUserCase @Inject constructor(private val repository: DataDragonRepository) {
    fun invoke(filersChamps: List<String> = listOf()) =
        repository.encyclopediaChampion()
}

class ChampionsRotationsUserCase @Inject constructor(private val repository: DataDragonRepository) {
    fun invoke() = repository.championsRotations()
}