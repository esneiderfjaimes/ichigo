package com.red.code015.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
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

fun Long.formatPts() = String.format("%,d", this).run {
    replace(',', if (length > 11) '.' else ' ') + if (length > 11) "pts" else " pts"
}

fun Long.formatPoints() = String.format("%,d", this).run {
    replace(',', if (length > 11) '.' else ' ')
}

val WindowInsets.Companion.isImeVisible: Boolean
    @Composable
    get() {
        val density = LocalDensity.current
        val ime = this.ime
        return remember {
            derivedStateOf {
                ime.getBottom(density) > 0
            }
        }.value
    }

fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect);
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom;
    return keypadHeight > screenHeight * 0.15
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
}

fun Modifier.clearFocusOnKeyboardDismiss2(): Modifier = composed {

    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val isKeyboardOpen by rememberIsKeyboardOpen()

        val focusManager = LocalFocusManager.current
        LaunchedEffect(isKeyboardOpen) {
            if (isKeyboardOpen) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}