package com.nei.ichigo.core.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile_icons",
    indices = [Index(value = ["version", "lang", "code"], unique = true)]
)
data class ProfileIconEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val version: String,
    val lang: String,
    val code: String,
    val image: String,
)
