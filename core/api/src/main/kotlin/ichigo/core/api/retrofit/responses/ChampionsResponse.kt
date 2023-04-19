package ichigo.core.api.retrofit.responses

import com.google.gson.annotations.SerializedName

data class ChampionsResponse(
    @SerializedName("type") val type: String?,
    @SerializedName("format") val format: String?,
    @SerializedName("version") val version: String?,
    @SerializedName("data") val data: Map<String, ChampionRS>?,
)

data class ChampionRS(
    @SerializedName("version") val version: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("image") val image: ImageRS?,
    @SerializedName("blurb") val blurb: String?,
    @SerializedName("info") val info: InfoRS?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("partype") val parType: String?,
    @SerializedName("stats") val stats: Map<String, Double>?,
)

data class ImageRS(
    val full: String?,
    val sprite: String?,
    val group: String?,
    val x: Long?,
    val y: Long?,
    val w: Long?,
    val h: Long?,
)

data class InfoRS(
    val attack: Long?,
    val defense: Long?,
    val magic: Long?,
    val difficulty: Long?,
)