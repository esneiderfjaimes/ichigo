package com.red.code015.usecases

import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.PlatformID
import com.red.code015.domain.RegionID




class HelperRepository constructor(
    private val remote: RemoteRiotGamesDataSource,
) {
    fun updateHost(platformID: PlatformID) {
        remote.updateHost(platformID)
    }

    fun updateHost(regionID: RegionID) {
        remote.updateHost(regionID)
    }
}