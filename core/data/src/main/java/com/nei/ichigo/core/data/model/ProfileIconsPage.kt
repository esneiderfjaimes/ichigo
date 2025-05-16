package com.nei.ichigo.core.data.model

import androidx.paging.PagingData
import com.nei.ichigo.core.model.ProfileIcon
import kotlinx.coroutines.flow.Flow

data class ProfileIconsPage(
    val version: String,
    val lang: String,
    val icons: List<ProfileIcon>,
    val pager: Flow<PagingData<ProfileIcon>>
)