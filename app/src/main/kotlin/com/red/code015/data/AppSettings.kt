package com.red.code015.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.protobuf.InvalidProtocolBufferException
import com.red.code015.domain.PlatformID
import com.red.code015.domain.Profile
import java.io.InputStream
import java.io.OutputStream

data class AppSettings(
    val loading: Boolean = false,
    @SerializedName("selected_platform")
    val platformID: PlatformID = PlatformID.NA,
    @SerializedName("registered_profiles")
    val profiles: List<Profile> = listOf(),
) {

    fun addOrUpdateProfile(profile: Profile): List<Profile> {
        val indexOfFirst =
            profiles.indexOfFirst { oldProfile -> oldProfile.puuID == profile.puuID }
        return if (indexOfFirst < 0) { // Add
            profiles.toMutableList().run {
                add(profile)
                toList()
            }
        } else { // Update
            profiles.toMutableList().run {
                this[indexOfFirst] = profile
                toList()
            }
        }
    }

    fun removeProfile(puuID: String): AppSettings {
        val indexOfFirst = profiles.indexOfFirst { sum -> sum.puuID == puuID }
        return if (indexOfFirst >= 0) {
            this.copy(
                profiles = profiles.toMutableList().run {
                    removeAt(indexOfFirst)
                    toList()
                }
            )
        } else this
    }

    fun writeTo(output: OutputStream, gson: Gson) {
        output.write(gson.toJson(this).encodeToByteArray())
    }

    companion object {
        fun getDefaultInstance() = AppSettings()

        fun parseFrom(input: InputStream, gson: Gson): AppSettings {
            val data: String = input.readBytes().decodeToString()
            return gson.fromJson(data, object : TypeToken<AppSettings>() {}.type)
        }
    }
}

object AppSettingsSerializer : Serializer<AppSettings> {
    private val gson: Gson = Gson()

    override val defaultValue: AppSettings = AppSettings.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): AppSettings {
        try {
            return AppSettings.parseFrom(input, gson)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) = t.writeTo(output, gson)
}
