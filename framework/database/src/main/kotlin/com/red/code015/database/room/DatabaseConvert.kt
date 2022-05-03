package com.red.code015.database.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.red.code015.domain.League
import java.util.*

open class LeaguesConverter {

    private val gson: Gson = Gson()

    @TypeConverter
    fun leaguesToString(someObjects: List<League>): String? = gson.toJson(someObjects)

    @TypeConverter
    fun stringToLeagues(data: String?): List<League> {
        if (data == null) return Collections.emptyList()
        return try {
            gson.fromJson(data, object : TypeToken<List<League>>() {}.type)
        } catch (e: Exception) {
            Collections.emptyList()
        }
    }

    @TypeConverter
    fun masteriesToString(someObjects: List<MasteryEntity>): String? = gson.toJson(someObjects)

    @TypeConverter
    fun stringToMasteries(data: String?): List<MasteryEntity> {
        if (data == null) return Collections.emptyList()
        return try {
            gson.fromJson(data, object : TypeToken<List<MasteryEntity>>() {}.type)
        } catch (e: Exception) {
            Collections.emptyList()
        }
    }

}