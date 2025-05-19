package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.model.ProfileIconsPage
import com.nei.ichigo.core.domain.GetProfileIconsUseCase
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState.PageInfo
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
import kotlinx.coroutines.launch

@HiltViewModel
class IconsViewModel @Inject constructor(
    getProfileIconsUseCase: GetProfileIconsUseCase
) : ViewModel() {
    private val pageIndex = MutableStateFlow<Int?>(0)
    private val pageSize = MutableStateFlow(PageInfo.PAGE_SIZE_DEFAULT)

    val uiState: StateFlow<IconsUiState> =
        combine(getProfileIconsUseCase(), pageIndex, pageSize) { pageResult, pageIndex, pageSize ->
            pageResult.fold(
                onSuccess = { page ->
                    mapper(page, pageIndex, pageSize)
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

    private fun mapper(
        page: ProfileIconsPage,
        pageIndex: Int?,
        pageSize: Int
    ): IconsUiState.Success {
        val (icons, pageInfo) = if (pageIndex != null) {
            val pageIcons = page.icons.getPage(pageSize = pageSize, pageIndex = pageIndex)
            val totalPages = (page.icons.size + pageSize - 1) / pageSize
            pageIcons to PageInfo(
                pageIndex = pageIndex,
                totalPages = totalPages,
                pageSize = pageSize
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
        viewModelScope.launch(Dispatchers.IO) {
            pageIndex.update { page }
        }
    }

    fun onPageSizeChange(pageSize: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            this@IconsViewModel.pageSize.update { pageSize }
        }
    }

    @Stable
    sealed interface IconsUiState {
        @Stable
        data object Loading : IconsUiState

        @Stable
        data class Success(
            val version: String,
            val lang: String,
            val icons: List<IconUi>,
            val totalIcons: Int,
            val pageInfo: PageInfo?
        ) : IconsUiState

        @Stable
        data class PageInfo(
            val pageIndex: Int,
            val totalPages: Int,
            val pageSize: Int,
        ) {
            companion object {
                const val PAGE_SIZE_DEFAULT = 25
                val PAGE_SIZES = listOf(25, 50, 100)
            }
        }

        @Stable
        data object Error : IconsUiState
    }
}