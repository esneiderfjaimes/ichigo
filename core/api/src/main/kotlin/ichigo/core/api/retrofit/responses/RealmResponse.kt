package ichigo.core.api.retrofit.responses

import com.google.gson.annotations.SerializedName

data class RealmResponse(
    @SerializedName("n") var data: RealmsDataRS?,
    @SerializedName("v") var version: String?,
    @SerializedName("l") var lenguage: String?,
    @SerializedName("dd") var dataDragonVersion: String?,
)

data class RealmsDataRS(
    @SerializedName("item") var itemVersion: String?,
    @SerializedName("rune") var runeVersion: String?,
    @SerializedName("mastery") var masteryVersion: String?,
    @SerializedName("summoner") var summonerVersion: String?,
    @SerializedName("champion") var championVersion: String?,
    @SerializedName("profileicon") var profileIconVersion: String?,
    @SerializedName("map") var mapVersion: String?,
    @SerializedName("language") var languageVersion: String?,
    @SerializedName("sticker") var stickerVersion: String?
)