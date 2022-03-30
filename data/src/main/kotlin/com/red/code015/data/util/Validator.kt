package com.red.code015.data.util

import java.util.*

fun requiresRemoteUpdate(lastCheckDate: Long): Boolean =
    Calendar.getInstance().apply {
        time = Date().apply { time = lastCheckDate }
        add(Calendar.DAY_OF_YEAR, 1)
    }.time.before(Date())
