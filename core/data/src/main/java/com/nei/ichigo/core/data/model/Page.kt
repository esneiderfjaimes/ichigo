package com.nei.ichigo.core.data.model

data class Page<T>(val version: String, val lang: String, val data: List<T>)