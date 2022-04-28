package com.red.code015.data.util

import java.util.*

fun requireRemoteFetch(lastCheckDate: Long?, force: Boolean = false): Boolean {
    lastCheckDate ?: return true
    return Calendar.getInstance().apply {
        time = Date().apply { time = lastCheckDate }
        add(Calendar.DAY_OF_YEAR, 7)
    }.time.before(Date()) || force
}

fun requireRemoteFetch(lastCheckDate: Long?, days: Int, force: Boolean = false): Boolean {
    lastCheckDate ?: return true
    return Calendar.getInstance().apply {
        time = Date().apply { time = lastCheckDate }
        add(Calendar.DAY_OF_YEAR, days)
    }.time.before(Date()) || force
}

