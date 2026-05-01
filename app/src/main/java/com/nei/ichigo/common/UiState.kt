package com.nei.ichigo.common

import androidx.annotation.StringRes
/*

sealed class UiState<T>(open val isRefreshing: Boolean) {
    data object Loading : UiState<Nothing>(false)
    data class Success<T>(val content: T, override val isRefreshing: Boolean = false) : UiState<T>(isRefreshing)
    data class Error(override val isRefreshing: Boolean = false) : UiState<Nothing>(isRefreshing)
}
*/

sealed class UiState<out T>(open val isRefreshing: Boolean) {
    data object Loading : UiState<Nothing>(false)

    data class Success<T>(
        val content: T,
        override val isRefreshing: Boolean = false
    ) : UiState<T>(isRefreshing)

    data class Error(
        @param:StringRes val messageRes: Int,
        val throwable: Throwable? = null,
        override val isRefreshing: Boolean = false
    ) : UiState<Nothing>(isRefreshing)
}

inline fun <T> UiState<T>.onSuccess(action: (T) -> Unit): UiState<T> =
    if (this is UiState.Success) {
        action(this.content)
        this
    } else {
        this
    }

fun <T> T.toSuccessUiState() = UiState.Success(this)

interface PageUiState {
    val version: String
}