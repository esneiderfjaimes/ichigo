package com.red.code015.usecases

import com.red.code015.data.repositories.DataDragonRepository


class LatestVersionUseCase constructor(private val repository: DataDragonRepository) {
    suspend fun invoke() = repository.lastVersion()
}

class EncyclopediaChampionUserCase constructor(private val repository: DataDragonRepository) {
    fun invoke(filersChamps: List<String> = listOf()) =
        repository.encyclopediaChampion()
}

class ChampionsRotationsUserCase constructor(private val repository: DataDragonRepository) {
    fun invoke() = repository.championsRotations()
}