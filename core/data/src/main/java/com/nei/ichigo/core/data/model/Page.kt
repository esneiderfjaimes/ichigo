package com.nei.ichigo.core.data.model

open class Page<T>(
    open val version: String,
    open val lang: String,
    open val data: T
)

data class ListPage<T>(
    override val version: String,
    override val lang: String,
    override val data: List<T>
) : Page<List<T>>(version, lang, data)