package com.red.code015.domain

data class SummonerSummary(
    val id: String,
    val accountId: String,
    val puuId: String,
    val name: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val level: Int,
    val lastCheckDate: Long,
    val dataSource: DataSource
)

enum class DataSource {
    REMOTE,
    LOCAL
}