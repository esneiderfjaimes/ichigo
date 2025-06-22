package com.nei.ichigo.feature.encyclopedia.icons

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.Base2ViewModel
import com.nei.ichigo.common.PageUI
import com.nei.ichigo.core.data.model.ProfileIconsPage
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
    getProfileIconsUseCase: GetProfileIconsUseCase
) : Base2ViewModel<IconsUiState>() {

    private val pageIndex = MutableStateFlow<Int?>(0)
    private val pageSize = MutableStateFlow(PAGE_SIZE_DEFAULT)

    override val flow = combine(
        flow = getProfileIconsUseCase(),
        flow2 = pageIndex,
        flow3 = pageSize,
        transform = ::mapper
    )

    private fun mapper(
        pageResult: Result<ProfileIconsPage>,
        pageIndex: Int?,
        pageSize: Int
    ): IconsUiState {
        val page = pageResult.getOrThrow()
        Log.d(
            "IconsViewModel",
            "mapper() called with: page = $page, pageIndex = $pageIndex, pageSize = $pageSize"
        )
        val (icons, pageInfo) = if (pageIndex != null) {
            val pageIcons = page.icons.getPage(pageSize = pageSize, pageIndex = pageIndex)
            val totalPages = (page.icons.size + pageSize - 1) / pageSize
            pageIcons to PageInfo(
                pageIndex = pageIndex,
                totalPages = totalPages
            )
        } else {
            page.icons to null
        }
        return IconsUiState(
            icons = icons.map(ProfileIcon::toUi),
            totalIcons = page.icons.size,
            pageInfo = pageInfo,
            pageSize = pageSize,
            version = page.version,
            lang = page.lang
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

    @Stable
    data class IconsUiState(
        val icons: List<IconUi>,
        val totalIcons: Int,
        val pageInfo: PageInfo?,
        val pageSize: Int,
        override val version: String,
        override val lang: String,
    ) : PageUI {
        companion object {
            const val PAGE_SIZE_DEFAULT = 25
            val PAGE_SIZES = listOf(25, 50, 100)
        }
    }
}