package com.red.code015.domain

enum class DataSources {
    API, DATABASE, PRELOAD
}

data class DataSource(val dataSources: DataSources, val time: Long)
