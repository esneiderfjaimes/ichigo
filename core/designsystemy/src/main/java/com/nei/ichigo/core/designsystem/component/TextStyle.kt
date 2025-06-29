package com.nei.ichigo.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@SuppressLint("ComposableNaming")
@Composable
fun AnnotatedString.Builder.appendTitle(text: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    ) {
        append(text)
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun AnnotatedString.Builder.appendVersion(text: String) {
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    ) {
        if (text.isNotBlank()) {
            append(" v${text}")
        }
    }
}