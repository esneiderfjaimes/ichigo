package com.nei.ichigo.core.data.repository

import kotlinx.coroutines.flow.Flow

interface DDragonRepository {

    val metaData: Flow<Result<MetaData>>

    suspend fun forceUpdate()

    data class MetaData(
        val versions: List<String>,
        val languages: List<String>
    )
}