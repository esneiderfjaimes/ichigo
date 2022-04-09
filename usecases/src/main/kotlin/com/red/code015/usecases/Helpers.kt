package com.red.code015.usecases

import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.PlatformID
import com.red.code015.domain.RegionID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelperRepository @Inject constructor(
    private val remote: RemoteRiotGamesDataSource,
) {
    fun updateHost(platformID: PlatformID) {
        remote.updateHost(platformID)
    }

    fun updateHost(regionID: RegionID) {
        remote.updateHost(regionID)
    }
}