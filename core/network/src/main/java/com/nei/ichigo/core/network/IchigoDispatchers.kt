package com.nei.ichigo.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val ichigoDispatcher: IchigoDispatchers)

enum class IchigoDispatchers {
    Default,
    IO,
}
