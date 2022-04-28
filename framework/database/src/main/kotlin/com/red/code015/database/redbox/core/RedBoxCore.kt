package com.red.code015.database.redbox.core

import android.app.Application
import com.google.gson.Gson
import java.io.File

interface BaseDao<T> {
    suspend fun insert(value: T, suffix: String = "")
    suspend fun read(suffix: String = ""): T?
}

open class BaseRB<T : Any>(
    private val cls: Class<T>,
    private val nameFile: String,
    app: Application,
    private val gson: Gson,
) : BaseDao<T> {

    private var dir: File = File(app.filesDir, "redbox/$nameFile")

    init {
        dir.mkdirs()
    }

    override suspend fun insert(value: T, suffix: String) {
        File(dir, getFileName(suffix)).writeText(gson.toJson(value))
    }

    private fun getFileName(suffix: String) =
        if (suffix.isBlank()) "$nameFile.json"
        else "$nameFile - $suffix.json"

    override suspend fun read(suffix: String): T? = try {
        val json = File(dir, getFileName(suffix))
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
        gson.fromJson(json, cls)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
