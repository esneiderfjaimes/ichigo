package com.nei.ichigo.feature.encyclopedia.runes

import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.data.model.ListPage
import com.nei.ichigo.core.domain.GetRunesUseCase
import com.nei.ichigo.core.model.RuneBranch
import com.nei.ichigo.feature.encyclopedia.runes.RunesViewModel.RunesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class RunesViewModel @Inject constructor(
    getRunesUseCase: GetRunesUseCase,
) : UiStateViewModel<RunesUiState>() {
    override val flow = getRunesUseCase().map { pageResult ->
        val page: ListPage<RuneBranch> = pageResult.getOrThrow()
        val branches: List<RuneBranch> = page.data.sortedByIdOrder(RuneBranch::id, ORDER)
        val version: String = page.version
        RunesUiState(
            branches = branches,
            version = version,
        )
    }

    data class RunesUiState(
        val branches: List<RuneBranch>,
        override val version: String,
    ) : PageUiState

    companion object {
        private val ORDER = listOf(
            "8000",
            "8100",
            "8200",
            "8400",
            "8300",
        )
    }
}

fun <T> List<T>.sortedByIdOrder(
    getId: (T) -> String,
    idOrder: List<String>
): List<T> {
    val orderMap = idOrder.withIndex().associate { it.value to it.index }

    return this.sortedWith(
        compareBy(
            { orderMap[it.let(getId)] ?: Int.MAX_VALUE },
            { this.indexOf(it) }
        )
    )
}
