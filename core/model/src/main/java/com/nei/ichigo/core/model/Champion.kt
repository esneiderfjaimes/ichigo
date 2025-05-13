package com.nei.ichigo.core.model

data class Champion(
    val version: String,
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val blurb: String,
    val image: Image,
    val tags: List<String>,
    val partype: String,
)

data class Image(
    val full: String,
    val sprite: String,
    val group: String,
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
)