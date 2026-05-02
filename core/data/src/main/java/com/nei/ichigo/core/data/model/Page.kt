package com.nei.ichigo.core.data.model

interface Base<T> {
    val version: String
    val lang: String
    val data: T
}

data class Page<T>(
    override val version: String,
    override val lang: String,
    override val data: T
) : Base<T>

data class ListPage<T>(
    override val version: String,
    override val lang: String,
    override val data: List<T>
) : Base<List<T>>