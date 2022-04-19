package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.PreloadDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG_LOGS
import com.red.code015.domain.EncyclopediaChampion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataDragonRepository @Inject constructor(
    private val remote: RemoteRiotGamesDataSource,
    private val preload: PreloadDataSource,
) {

    companion object {
        const val tag = "$TAG_LOGS:ProfileRep"
        var count = 0
    }

    init {
        count++
        Log.w(tag, "instance of ProfileRepository($count)")
    }

    fun encyclopediaChampion(lang: String = "en_US"): Flow<EncyclopediaChampion> = flow {
        try {
            emit(preload.encyclopediaChampion(lang))
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                emit(remote.encyclopediaChampion(lang))
            } catch (e2: Exception) {
                throw e2
            }
        }
    }

}

