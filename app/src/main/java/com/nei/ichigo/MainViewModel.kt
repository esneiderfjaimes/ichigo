package com.nei.ichigo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.MainUiState.Loading
import com.nei.ichigo.MainUiState.Success
import com.nei.ichigo.core.data.repository.UserSettingsRepository
import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.core.model.UserSettings
import com.nei.ichigo.feature.settings.navigation.EncyclopediaSettingsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = userSettingsRepository.userSettings.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    suspend fun getLastNavigationRoute() =
        userSettingsRepository.userSettings.first().lastNavigationRoute

    fun updateLastNavigationRoute(navKey: NavKey) {
        viewModelScope.launch(Dispatchers.IO) {
            // We don't want to update the last navigation route when the user is on the settings screen
            if (navKey == EncyclopediaSettingsRoute) return@launch
            // TODO: Update the last navigation route
            // userSettingsRepository.updateLastNavigationRoute(route.name)
        }
    }
}

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(val userData: UserSettings) : MainUiState {
        override val shouldDisableDynamicTheming = !userData.useDynamicColor

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }
    }

    /**
     * Returns `true` if the state wasn't loaded yet and it should keep showing the splash screen.
     */
    fun shouldKeepSplashScreen() = this is Loading

    /**
     * Returns `true` if the dynamic color is disabled.
     */
    val shouldDisableDynamicTheming: Boolean get() = true

    /**
     * Returns `true` if dark theme should be used.
     */
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}
