package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.designsystem.component.PageInfo
import com.nei.ichigo.core.domain.GetProfileIconsUseCase
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState.Companion.PAGE_SIZE_DEFAULT
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class IconsViewModel @Inject constructor(
    private val getProfileIconsUseCase: GetProfileIconsUseCase
) : UiStateViewModel<IconsUiState>() {

    private val pageIndex = MutableStateFlow<Int?>(0)
    private val pageSize = MutableStateFlow(PAGE_SIZE_DEFAULT)

    override fun getFlow() = combine(
        flow = getProfileIconsUseCase(),
        flow2 = pageIndex,
        flow3 = pageSize,
    ) { pageResult, pageIndex, pageSize ->
        val page = pageResult.getOrThrow()
        val (icons, pageInfo) = if (pageIndex != null) {
            val pageIcons = page.data.getPage(pageSize = pageSize, pageIndex = pageIndex)
            val totalPages = (page.data.size + pageSize - 1) / pageSize
            pageIcons to PageInfo(
                pageIndex = pageIndex,
                totalPages = totalPages
            )
        } else {
            page.data to null
        }
        IconsUiState(
            icons = icons.map(ProfileIcon::toUi),
            totalIcons = page.data.size,
            pageInfo = pageInfo,
            pageSize = pageSize,
            version = page.version
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

    fun onPageSizeChange(newPageSize: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val previousIndex = pageIndex.value
            if (previousIndex != null) {
                val oldPageSize = this@IconsViewModel.pageSize.value
                val newPageIndex = recalculatePageIndex(previousIndex, oldPageSize, newPageSize)
                pageIndex.update { newPageIndex }
            }
            pageSize.update { newPageSize }
        }
    }

    private fun recalculatePageIndex(previousIndex: Int, oldPageSize: Int, newPageSize: Int): Int {
        val itemPosition = previousIndex * oldPageSize
        return itemPosition / newPageSize
    }

    @Immutable
    data class IconsUiState(
        val icons: List<IconUi>,
        val totalIcons: Int,
        val pageInfo: PageInfo?,
        val pageSize: Int,
        override val version: String,
    ) : PageUiState {
        companion object {
            const val PAGE_SIZE_DEFAULT = 25
            val PAGE_SIZES = listOf(25, 50, 100)
        }
    }
}