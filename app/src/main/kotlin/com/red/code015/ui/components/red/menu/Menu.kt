@file:OptIn(ExperimentalMaterial3Api::class)

package com.red.code015.ui.components.red.menu

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties


/*@Composable
fun <T> Menu(items: List<T>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        offset = DpOffset(with(LocalDensity.current) { asd.width.toDp() + yoffset.x },
            yoffset.y),
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp)
            .onGloballyPositioned {
                asd2 = it.size.toSize()
            }
    ) {
        listOf("Grid", "List").forEach { action ->
            DropdownMenuItem(text = {
                Text(text = action)
            }, onClick = {
                onExpandedChange(false)
            })
        }
    }
}*/

data class ItemUI<T>(val id: T, val text: String)

class MenuStateHolder<T>(val items: List<ItemUI<T>>) {

    var selectedText by mutableStateOf("")
    var expanded by mutableStateOf(false)
    var selectedIndex by mutableStateOf(-1)
    var size by mutableStateOf(Size.Zero)


    fun onSelectedIndex(int: Int) {
        selectedIndex = int
    }

    fun onSelectedIndex(size: Size) {
        this.size = size
    }

    fun show() {
        expanded = true
    }

    fun hide() {
        expanded = false
    }
}

@Composable
fun <T> rememberState(items: List<ItemUI<T>>) = remember {
    MenuStateHolder(items)
}


@Composable
fun <T> Menu(items: Map<T, String>, selected: T, onClick: (T) -> Unit = {}) {
    Menu(items = items.map { ItemUI(it.key, it.value) }, selected = selected, onClick)
}

@Composable
fun <T> Menu(items: List<ItemUI<T>>, selected: T, onClick: (T) -> Unit = {}) {
    val state = rememberState(items)

    val indexOfFirst = items.indexOfFirst { it.id == selected }

    FilledTonalButton(
        onClick = { state.show() },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { state.onSelectedIndex(it.size.toSize()) }
            .animateContentSize(),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(0.125f)
        ),
    ) {
        Text(
            text = if (indexOfFirst != -1) items[indexOfFirst].text
            else "No selected",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium
        )

        DropdownMenu(expanded = state.expanded,
            onDismissRequest = { state.hide() },
            modifier = Modifier
            // TODO
            // .width(with(LocalDensity.current) { state.size.width.toDp() })
        ) {
            state.items.forEach {
                val (id, text) = it
                DropdownMenuItem(
                    text = { Text(text = text, style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        onClick(id)
                        state.hide()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomSheetPop(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    sheetContent: @Composable BoxScope.() -> Unit,
) {
    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded

    if (expandedStates.currentState || expandedStates.targetState) {
        val transformOriginState = remember { mutableStateOf(TransformOrigin.Center) }
        val density = LocalDensity.current
        val popupPositionProvider = DropdownMenuPositionProvider2(
            offset,
            density
        ) { parentBounds, menuBounds ->
            transformOriginState.value = calculateTransformOrigin(parentBounds, menuBounds)
        }

        val transition = updateTransition(expandedStates, "DropDownMenu")

        val alpha by transition.animateFloat(
            transitionSpec = {
                if (false isTransitioningTo true) {
                    // Dismissed to expanded
                    tween(durationMillis = 500)
                } else {
                    // Expanded to dismissed.
                    tween(durationMillis = 250)
                }
            }, label = ""
        ) {
            if (it) {
                // Menu is expanded.
                0.5f
            } else {
                // Menu is dismissed.
                0f
            }
        }

        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = properties
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                )
                .alpha(alpha * 2),
                contentAlignment = Alignment.BottomCenter) {
                DropdownMenuContent(
                    modifier = modifier,
                    content = sheetContent
                )
            }
        }
    }
}

@Composable
fun DropdownMenuContent(
    modifier: Modifier = Modifier,
    content: @Composable() (BoxScope.() -> Unit),
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 33.dp, topStart = 33.dp),
    ) {
        Box(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .verticalScroll(rememberScrollState()),
        ) {
            content(this)
        }
    }
}

internal data class DropdownMenuPositionProvider2(
    val contentOffset: DpOffset,
    val density: Density,
    val onPositionCalculated: (IntRect, IntRect) -> Unit = { _, _ -> },
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        // The min margin above and below the menu, relative to the screen.
        val verticalMargin = with(density) { 48.dp.roundToPx() }
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }

        // Compute horizontal position.
        val toRight = anchorBounds.left + contentOffsetX
        val toLeft = anchorBounds.right - contentOffsetX - popupContentSize.width
        val toDisplayRight = windowSize.width - popupContentSize.width
        val toDisplayLeft = 0
        val x = if (layoutDirection == LayoutDirection.Ltr) {
            sequenceOf(
                toRight,
                toLeft,
                // If the anchor gets outside of the window on the left, we want to position
                // toDisplayLeft for proximity to the anchor. Otherwise, toDisplayRight.
                if (anchorBounds.left >= 0) toDisplayRight else toDisplayLeft
            )
        } else {
            sequenceOf(
                toLeft,
                toRight,
                // If the anchor gets outside of the window on the right, we want to position
                // toDisplayRight for proximity to the anchor. Otherwise, toDisplayLeft.
                if (anchorBounds.right <= windowSize.width) toDisplayLeft else toDisplayRight
            )
        }.firstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: toLeft

        // Compute vertical position.
        val toBottom = maxOf(anchorBounds.bottom + contentOffsetY, verticalMargin)
        val toTop = anchorBounds.top - contentOffsetY - popupContentSize.height
        val toCenter = anchorBounds.top - popupContentSize.height / 2
        val toDisplayBottom = windowSize.height - popupContentSize.height - verticalMargin
        val y = sequenceOf(toBottom, toTop, toCenter, toDisplayBottom).firstOrNull {
            it >= verticalMargin &&
                    it + popupContentSize.height <= windowSize.height - verticalMargin
        } ?: toTop

        onPositionCalculated(
            anchorBounds,
            IntRect(x, y, x + popupContentSize.width, y + popupContentSize.height)
        )
        return IntOffset(x, y)
    }
}

internal fun calculateTransformOrigin(
    parentBounds: IntRect,
    menuBounds: IntRect,
): TransformOrigin {
    val pivotX = when {
        menuBounds.left >= parentBounds.right -> 0f
        menuBounds.right <= parentBounds.left -> 1f
        menuBounds.width == 0 -> 0f
        else -> {
            val intersectionCenter =
                (kotlin.math.max(parentBounds.left, menuBounds.left) +
                        kotlin.math.min(parentBounds.right, menuBounds.right)) / 2
            (intersectionCenter - menuBounds.left).toFloat() / menuBounds.width
        }
    }
    val pivotY = when {
        menuBounds.top >= parentBounds.bottom -> 0f
        menuBounds.bottom <= parentBounds.top -> 1f
        menuBounds.height == 0 -> 0f
        else -> {
            val intersectionCenter =
                (kotlin.math.max(parentBounds.top, menuBounds.top) +
                                kotlin.math.min(parentBounds.bottom, menuBounds.bottom)) / 2
            (intersectionCenter - menuBounds.top).toFloat() / menuBounds.height
        }
    }
    return TransformOrigin(pivotX, pivotY)
}