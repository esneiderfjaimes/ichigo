package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG_LOGS
import com.red.code015.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val remote: RemoteRiotGamesDataSource,
) {

    companion object {
        const val tag = "$TAG_LOGS:ProfileRep"
        var count = 0
    }

    init {
        count++
        Log.w(tag, "instance of ProfileRepository($count)")
    }

    fun bySummonerName(name: String): Flow<Profile> = flow {
        try {
            emit(remote.profileBySummonerName(name))
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }

    fun byRiotID(name: String, tagline: String): Flow<Profile> = flow {
        try {
            emit(remote.profileByRiotID(name, tagline))
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }

}