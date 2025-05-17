package com.nei.ichigo.feature.encyclopedia.icons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.domain.GetProfileIconsUseCase
import com.nei.ichigo.core.model.ProfileIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class IconsViewModel @Inject constructor(
    getProfileIconsUseCase: GetProfileIconsUseCase
) : ViewModel() {

    val uiState: StateFlow<IconsUiState> =
        getProfileIconsUseCase().map { pageResult ->
            pageResult.fold(
                onSuccess = { page ->
                    IconsUiState.Success(
                        version = page.version,
                        lang = page.lang,
                        icons = page.icons,
                    )
                },
                onFailure = {
                    it.printStackTrace()
                    IconsUiState.Error
                }
            )
        }.catch {
            it.printStackTrace()
            emit(IconsUiState.Error)
        }.flowOn(Dispatchers.IO).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = IconsUiState.Loading,
        )

    sealed interface IconsUiState {
        data object Loading : IconsUiState
        data class Success(
            val version: String,
            val lang: String,
            val icons: List<ProfileIcon>,
        ) : IconsUiState

        data object Error : IconsUiState
    }
}