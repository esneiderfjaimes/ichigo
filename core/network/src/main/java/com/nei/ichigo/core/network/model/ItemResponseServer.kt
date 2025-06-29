package com.nei.ichigo.core.network.model

import com.google.gson.annotations.SerializedName
import com.nei.ichigo.core.model.Gold
import com.nei.ichigo.core.model.Item

/*"""
{
      "name": "Botas",
      "description": "\u003CmainText\u003E\u003Cstats\u003E\u003Cattention\u003E25\u003C/attention\u003E de Velocidad de Movimiento\u003C/stats\u003E\u003Cbr\u003E\u003Cbr\u003E\u003C/mainText\u003E",
      "colloq": ";Boots of Speed",
      "plaintext": "Aumenta ligeramente la Velocidad de Movimiento.",
      "into": [
        "3005",
        "3047",
        "3006",
        "3009",
        "3010",
        "3020",
        "3111",
        "3117",
        "3158"
      ],
      "image": {
        "full": "1001.png",
        "sprite": "item0.png",
        "group": "item",
        "x": 0,
        "y": 0,
        "w": 48,
        "h": 48
      },
      "gold": {
        "base": 300,
        "purchasable": true,
        "total": 300,
        "sell": 210
      },
      "tags": [
        "Boots"
      ],
      "maps": {
        "11": true,
        "12": true,
        "21": true,
        "22": false,
        "30": false,
        "33": false,
        "35": true
      },
      "stats": {
        "FlatMovementSpeedMod": 25
      }
    }
"""*/

data class ItemResponseServer(
    @SerializedName("name") val name: String?,
    @SerializedName("plaintext") val plaintext: String?,
    @SerializedName("image") val image: ImageResponseServer?,
    @SerializedName("tag") val description: String?,
    @SerializedName("from") val from: List<String>?,
    @SerializedName("into") val into: List<String>?,
    @SerializedName("maps") val maps: Map<String, Boolean>?,
    @SerializedName("gold") val gold: GoldResponseServer?,
)

data class GoldResponseServer(
    val base: Int,
    val purchasable: Boolean,
    val total: Int,
    val sell: Int
)

fun ItemResponseServer.asExternalModel(id: String) = Item(
    id = id,
    name = name!!,
    plaintext = plaintext!!,
    image = image!!.full!!,
    description = description ?: "",
    from = from ?: emptyList(),
    into = into ?: emptyList(),
    maps = maps?.filterValues { it }?.keys ?: emptySet(),
    gold = gold!!.asExternalModel()
)

fun GoldResponseServer.asExternalModel() = Gold(
    base = base,
    purchasable = purchasable,
    total = total,
    sell = sell
)