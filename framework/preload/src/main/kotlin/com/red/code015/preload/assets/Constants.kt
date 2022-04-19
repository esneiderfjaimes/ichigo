package com.red.code015.preload.assets

const val TAG = "RD:Preload"

const val ForceImageFetch = false

const val FolderChampsThumbnail = "champions_thumbnail"
const val FolderData = "data"

fun championsFileName(lang: String) = "$FolderData/$lang/champions.json"
fun championsOriginalFileName(lang: String) = "$FolderData/$lang/champions_original.json"