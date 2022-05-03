package com.red.code015.database.redbox.core

import android.app.Application
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException

interface BaseDao<T> {
    suspend fun insert(value: T, suffix: String = "")
    suspend fun insert(value: T, suffix: String = "", vararg prefixes: String = arrayOf())
    suspend fun read(suffix: String = ""): T?
    suspend fun read(suffix: String = "", vararg prefixes: String = arrayOf()): T?
}

open class BaseRB<T : Any>(
    private val cls: Class<T>,
    private val nameFile: String,
    app: Application,
    private val gson: Gson,
    private val nameDir: String? = null,
) : BaseDao<T> {

    private var dir: File = File(app.filesDir, "redbox/${nameDir ?: nameFile}")

    init {
        dir.mkdirs()
    }

    override suspend fun insert(value: T, suffix: String) {
        File(dir, getFileName(suffix)).writeText(gson.toJson(value))
    }

    private fun getFileName(suffix: String) =
        if (suffix.isBlank()) "$nameFile.json"
        else "$nameFile - $suffix.json"

    private fun getFileName2(suffix: String) =
        if (suffix.isBlank()) "/$nameFile.json"
        else "/$suffix.json"

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

    override suspend fun read(suffix: String, vararg prefixes: String): T? = try {
        val folders = prefixes.joinToString("/")
        val json = File(dir, folders + getFileName2(suffix))
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
        gson.fromJson(json, cls)
    } catch (e: Exception) {
        if (e is FileNotFoundException) null
        else {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun insert(value: T, suffix: String, vararg prefixes: String) {
        val folders = prefixes.joinToString("/")
        File(dir, folders + getFileName2(suffix))
            .apply { parentFile?.mkdirs() }
            .writeText(gson.toJson(value))
    }
}
