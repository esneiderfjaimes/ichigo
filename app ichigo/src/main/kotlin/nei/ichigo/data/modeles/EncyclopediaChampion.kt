package nei.ichigo.data.modeles

import android.graphics.Bitmap

data class ChampListItem(
    val id: String,
    val name: String,
    val image: Image,
)

data class Image(
    val bitmap: Bitmap?,
    val full: String,
    val sprite: String,
    val group: String,
    val x: Long,
    val y: Long,
    val w: Long,
    val h: Long,
) {
    companion object {
        val default = Image(null, "", "", "", 0, 0, 0, 0)
    }
}