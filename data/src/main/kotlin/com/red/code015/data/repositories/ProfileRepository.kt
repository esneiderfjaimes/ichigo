package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.APIDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG
import com.red.code015.domain.Profile
import kotlinx.coroutines.flow.Flow




class ProfileRepository constructor(
    source: APIDataSource,
    private val remote: RemoteRiotGamesDataSource,
) : API(source) {

    companion object {
        var count = 0
    }

    init {
        count++
        Log.w("$TAG:PF", "instance of ProfileRepository($count)")
    }

    fun bySummonerName(name: String): Flow<Profile> = flowTryRemote {
        emit(remote.profileBySummonerName(name))
    }

    fun byRiotID(name: String, tagline: String): Flow<Profile> = flowTryRemote {
        emit(remote.profileByRiotID(name, tagline))
    }

}