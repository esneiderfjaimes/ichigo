package com.red.code015.ui.pages.encyclopedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.usecases.LatestVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
@OptIn(InternalCoroutinesApi::class)
class EncyclopediaViewModel @Inject constructor(
    latestVersionUseCase: LatestVersionUseCase,
) : ViewModel() {

    //region Fields

    private val _lastVersion = MutableStateFlow("")
    val latestVersion: StateFlow<String> = _lastVersion

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _lastVersion.value = latestVersionUseCase.invoke()
            }
        }
    }

    // endregion

}