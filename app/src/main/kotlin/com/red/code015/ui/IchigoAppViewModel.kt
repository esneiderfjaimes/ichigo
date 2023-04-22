package com.red.code015.ui

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.data.AppSettings
import com.red.code015.domain.PlatformID
import com.red.code015.domain.Profile
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class IchigoAppViewModel constructor(
    private val settingsDataStore: DataStore<AppSettings>,
) : ViewModel() {

    val flowAppSettings: Flow<AppSettings> = settingsDataStore.data

    fun setLanguage(platformID: PlatformID) {
        viewModelScope.launch {
            settingsDataStore.updateData { it.copy(platformID = platformID) }
        }
    }

    fun addOrUpdateProfile(profile: Profile) {
        viewModelScope.launch {
            settingsDataStore.updateData { it.copy(profiles = it.addOrUpdateProfile(profile)) }
        }
    }

    fun removeProfile(puuID: String) {
        viewModelScope.launch {
            settingsDataStore.updateData {
                it.removeProfile(puuID)
            }
        }
    }

}