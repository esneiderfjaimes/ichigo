package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.data.model.Config
import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {

    val config: Flow<Result<Config>>

}
