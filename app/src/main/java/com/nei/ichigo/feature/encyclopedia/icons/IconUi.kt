package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import com.nei.ichigo.core.model.ProfileIcon

@Immutable
data class IconUi(
    val id: String,
    val image: String,
) {
    override fun equals(other: Any?): Boolean {
        if (other is IconUi) {
            return id == other.id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    companion object {
        val Saver = object : Saver<IconUi?, List<Any>> {
            override fun restore(value: List<Any>): IconUi? {
                if (value.isEmpty()) return null
                return IconUi(
                    id = value[0] as String,
                    image = value[1] as String
                )
            }

            override fun SaverScope.save(value: IconUi?): List<Any> {
                if (value == null) return emptyList()
                return listOf(value.id, value.image)
            }
        }
    }
}

fun ProfileIcon.toUi() = IconUi(id = id, image = image)