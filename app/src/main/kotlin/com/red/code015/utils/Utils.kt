package com.red.code015.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.*

class PrefixTransformation(private val prefix: String, private val primary: Color) :
    VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = prefix + text.text.run {
            ifEmpty { "TAG" }
        }
        val prefixOffset = prefix.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + prefixOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= prefixOffset - 1) return prefixOffset
                return offset - prefixOffset
            }
        }

        return TransformedText(AnnotatedString(out.uppercase(Locale.getDefault()),
            AnnotatedString("#",
                SpanStyle(color = primary.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Black)).spanStyles), offsetMapping)
    }
}