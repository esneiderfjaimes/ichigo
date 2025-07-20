package com.nei.ichigo.common

sealed interface UiState<T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val content: T) : UiState<T>
    data object Error : UiState<Nothing>
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