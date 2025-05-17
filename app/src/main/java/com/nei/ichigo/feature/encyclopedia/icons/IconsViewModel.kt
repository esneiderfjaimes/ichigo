package com.nei.ichigo.feature.encyclopedia.icons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.model.ProfileIconsPage
import com.nei.ichigo.core.domain.GetProfileIconsUseCase
import com.nei.ichigo.core.model.ProfileIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private const val PAGE_SIZE = 25

@HiltViewModel
class IconsViewModel @Inject constructor(
    getProfileIconsUseCase: GetProfileIconsUseCase
) : ViewModel() {
    private val pageIndex = MutableStateFlow<Int?>(0)

    val uiState: StateFlow<IconsUiState> =
        combine(getProfileIconsUseCase(), pageIndex) { pageResult, pageIndex ->
            pageResult.fold(
                onSuccess = { page ->
                    mapper(page, pageIndex)

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

    private fun mapper(page: ProfileIconsPage, pageIndex: Int?): IconsUiState.Success {
        val (icons, pageInfo) = if (pageIndex != null) {
            val pageIcons = page.icons.getPage(pageSize = PAGE_SIZE, pageIndex = pageIndex)
            val totalPages = (page.icons.size + PAGE_SIZE - 1) / PAGE_SIZE
            pageIcons to IconsUiState.PageInfo(
                pageIndex = pageIndex,
                totalPages = totalPages,
            )
        } else {
            page.icons to null
        }
        return IconsUiState.Success(
            version = page.version,
            lang = page.lang,
            icons = icons.map(ProfileIcon::toUi),
            totalIcons = page.icons.size,
            pageInfo = pageInfo
        )
    }

    private fun <T> List<T>.getPage(pageSize: Int, pageIndex: Int): List<T> {
        val fromIndex = pageIndex * pageSize
        if (fromIndex >= size) return emptyList()
        return drop(fromIndex).take(pageSize)
    }

    fun onSelectPage(page: Int?) {
        pageIndex.update { page }
    }

    sealed interface IconsUiState {
        data object Loading : IconsUiState
        data class Success(
            val version: String,
            val lang: String,
            val icons: List<IconUi>,
            val totalIcons: Int,
            val pageInfo: PageInfo?
        ) : IconsUiState

        data class PageInfo(
            val pageIndex: Int,
            val totalPages: Int,
        )

        data object Error : IconsUiState
    }
}