package com.nei.ichigo.common

sealed interface UiState<T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val content: T) : UiState<T>
    data object Error : UiState<Nothing>
}

interface PageUiState {
    val version: String
}