package com.nei.ichigo.feature.encyclopedia.runes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.nei.ichigo.core.model.Rune

@Composable
fun RunesPopUp(
    currentItemId: String?,
    item: Rune,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = currentItemId == item.id,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 8.dp
    ) {
        RunesPopUpContent(item)
    }
}

@Composable
private fun RunesPopUpContent(item: Rune) {
    var showLongDesc by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier
            .sizeIn(maxWidth = 200.dp)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleMedium
                .copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        )
        val parsed = remember(showLongDesc) {
            parseRuneHtml(
                raw = if (showLongDesc) {
                    item.longDesc
                } else {
                    item.shortDesc
                }
            )
        }
        Surface(
            onClick = { showLongDesc = !showLongDesc },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(bottom = 4.dp)
        ) {
            Text(
                parsed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(4.dp)
                    .animateContentSize()
            )
        }
    }
}

fun parseRuneHtml(raw: String): AnnotatedString {
    val builder = AnnotatedString.Builder()

    val cleaned = raw
        .replace("<br>", "\n")
        .replace(Regex("</?[a-zA-Z0-9\\-_:]+.*?>")) { matchResult ->
            // Si el tag no es <b> o <font>, lo quitamos pero conservamos el contenido
            val tag = matchResult.value.lowercase()
            if (tag.startsWith("<font") || tag == "</font>" || tag == "<b>" || tag == "</b>") {
                matchResult.value
            } else {
                ""
            }
        }

    val fontRegex = Regex("<font color='(#[0-9A-Fa-f]{6})'>(.*?)</font>")

    val temp = cleaned
    val matches = fontRegex.findAll(temp).toList()

    var currentIndex = 0
    matches.forEach { match ->
        val start = match.range.first
        val end = match.range.last + 1

        if (currentIndex < start) {
            appendTextWithBoldSupport(builder, temp.substring(currentIndex, start))
        }

        val color = Color(match.groupValues[1].toColorInt())
        val content = match.groupValues[2]

        val spanStart = builder.length
        appendTextWithBoldSupport(builder, content)
        builder.addStyle(SpanStyle(color = color), spanStart, builder.length)

        currentIndex = end
    }

    if (currentIndex < temp.length) {
        appendTextWithBoldSupport(builder, temp.substring(currentIndex))
    }

    return builder.toAnnotatedString()
}

private fun appendTextWithBoldSupport(builder: AnnotatedString.Builder, input: String) {
    var currentIndex = 0
    val boldRegex = Regex("<b>(.*?)</b>")
    val matches = boldRegex.findAll(input).toList()

    matches.forEach { match ->
        val start = match.range.first
        val end = match.range.last + 1

        // Texto plano antes del <b>
        if (currentIndex < start) {
            builder.append(input.substring(currentIndex, start))
        }

        val content = match.groupValues[1]
        val spanStart = builder.length
        builder.append(content)
        builder.addStyle(SpanStyle(fontWeight = FontWeight.Bold), spanStart, builder.length)

        currentIndex = end
    }

    if (currentIndex < input.length) {
        builder.append(input.substring(currentIndex))
    }
}


@Preview
@Composable
private fun RunesPopUpPreview() {
    Surface {
        RunesPopUpContent(
            item = Rune(
                id = "1",
                name = "test",
                longDesc = "short desc",
                shortDesc = "Mientras estés por encima del 70% de vida, obtienes una bonificación <lol-uikit-tooltipped-keyword key='LinkTooltip_Description_Adaptive'><font color='#48C4B7'>adaptable</font></lol-uikit-tooltipped-keyword> de hasta 18 de daño de ataque o 30 de poder de habilidad (según el nivel). <br><br>Otorga 1.8 de daño de ataque o 3 de poder de habilidad a nivel 1.",
                icon = "test",
                key = "1",
            )
        )
    }
}