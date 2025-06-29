package com.nei.ichigo.feature.encyclopedia.items

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_BORDER_WIDTH
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.theme.Gold
import com.nei.ichigo.core.designsystem.utils.getItemImage
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState.ItemUi

private val ITEM_RECIPE_TREE_SIZE = DEFAULT_ITEM_SIZE * 0.75f
private val ITEM_RECIPE_TREE_SPACING = 8.dp
private val DECOR_HEIGHT = 4.dp
private val BORDER_TOTAL_SIZE = DEFAULT_ITEM_BORDER_WIDTH * 2
private val DECOR_WIDTH = ITEM_RECIPE_TREE_SIZE + ITEM_RECIPE_TREE_SPACING

enum class Decoration {
    SINGLE,
    START,
    INTERMEDIATE,
    END
}

@Composable
fun ItemPopUp(
    item: ItemUi,
    itemsMap: Map<String, ItemUi>,
    version: String,
    currentItemId: String?,
    onDismissRequest: () -> Unit,
    scrollToItem: (String) -> Unit
) {
    DropdownMenu(
        expanded = currentItemId == item.id,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp
    ) {
        ItemPopUpContent(
            item = item,
            itemsMap = itemsMap,
            version = version,
            scrollToItem = scrollToItem
        )
    }
}

@Composable
private fun ItemPopUpContent(
    item: ItemUi,
    itemsMap: Map<String, ItemUi>,
    version: String,
    scrollToItem: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        ItemPossibility(item, itemsMap, version) { id ->
            if (id == item.id) return@ItemPossibility
            scrollToItem(id)
        }

        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp)
        )

        ItemRecipeTree(item, itemsMap, version) { id ->
            if (id == item.id) return@ItemRecipeTree
            scrollToItem(id)
        }
    }
}

@Composable
fun ItemRecipeTree(
    item: ItemUi,
    itemsMap: Map<String, ItemUi>,
    version: String,
    decoration: Decoration? = null,
    onClick: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.drawBehind {
            if (decoration != null) {
                drawDecoration(decoration)
            }
        }
    ) {
        // space to draw the canvas
        if (decoration != null) {
            Spacer(modifier = Modifier.size(DECOR_WIDTH, DECOR_HEIGHT))
        }

        ItemTree(
            item = item,
            version = version,
            onClick = onClick
        )

        val children = item.from
        if (children.isNotEmpty()) {
            CanvasBase { verticalLine() }

            if (children.size == 1) {
                ItemRecipeTree(
                    item = itemsMap[children[0]]!!,
                    itemsMap = itemsMap,
                    version = version,
                    decoration = Decoration.SINGLE,
                    onClick = onClick
                )
            } else {
                Row {
                    children.forEachIndexed { index, id ->
                        val child = itemsMap[id] ?: return@forEachIndexed
                        val decorationType = when (index) {
                            0 -> Decoration.START
                            children.size - 1 -> Decoration.END
                            else -> Decoration.INTERMEDIATE
                        }
                        ItemRecipeTree(
                            item = child,
                            itemsMap = itemsMap,
                            version = version,
                            decoration = decorationType,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemPossibility(
    item: ItemUi,
    itemsMap: Map<String, ItemUi>,
    version: String,
    onClick: (String) -> Unit,
) {
    if (item.into.isEmpty()) {
        return
    }

    val chunked = item.into
        .mapNotNull { itemsMap[it] }
        .chunked(5)
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(bottom = ITEM_RECIPE_TREE_SPACING)
            .border(
                width = DEFAULT_ITEM_BORDER_WIDTH,
                color = Gold,
                shape = RectangleShape
            )
            .padding(4.dp)
    ) {
        chunked.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                // modifier = Modifier.padding(horizontal = BORDER_TOTAL_SIZE)
            ) {
                row.forEach { item ->
                    ItemTree(
                        item = item,
                        version = version,
                        sizePercentage = 0.5f,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun ItemTree(
    item: ItemUi,
    version: String,
    sizePercentage: Float = 0.75f,
    onClick: (String) -> Unit = {}
) {
    IchigoItemImage(
        model = getItemImage(item.image, version),
        modifier = Modifier
            .clickable { onClick(item.id) },
        shape = ITEM_SHAPE,
        size = DEFAULT_ITEM_SIZE * sizePercentage
    )
}

@Composable
private fun CanvasBase(getPath: DrawScope.() -> Path) {
    Canvas(modifier = Modifier.size(DECOR_WIDTH, DECOR_HEIGHT)) {
        val strokeWidth = BORDER_TOTAL_SIZE.toPx()
        drawPath(
            path = getPath(),
            color = Gold,
            style = Stroke(width = strokeWidth)
        )
    }
}

private fun DrawScope.drawDecoration(decorationType: Decoration) {
    val strokeWidth = BORDER_TOTAL_SIZE.toPx()
    drawPath(
        path = when (decorationType) {
            Decoration.START -> connectStart()
            Decoration.INTERMEDIATE -> connectIntermediate()
            Decoration.END -> connectEnd()
            Decoration.SINGLE -> verticalLine()
        },
        color = Gold,
        style = Stroke(width = strokeWidth)
    )
}

private fun DrawScope.verticalLine() = baseDecoration { width, height, centerX ->
    moveTo(centerX, 0f)
    lineTo(centerX, height)
}

private fun DrawScope.connectStart() = baseDecoration { width, height, centerX ->
    moveTo(centerX, height)
    quadraticTo(centerX, 0f, centerX + height, 0f)
    lineTo(width, 0f)
}

private fun DrawScope.connectEnd() = baseDecoration { _, height, centerX ->
    moveTo(centerX, height)
    quadraticTo(centerX, 0f, centerX - height, 0f)
    lineTo(0f, 0f)
}

private fun DrawScope.connectIntermediate() = baseDecoration { width, height, centerX ->
    moveTo(centerX, 0f)
    lineTo(centerX, height)
    moveTo(0f, 0f)
    lineTo(width, 0f)
}

private fun DrawScope.baseDecoration(path: Path.(width: Float, height: Float, centerX: Float) -> Unit) =
    Path().apply {
        val width = size.width
        val height = DECOR_HEIGHT.toPx()
        val centerX = width / 2f
        path(width, height, centerX)
    }


@Preview
@Composable
private fun ItemPopUpPreview() {
    val map = mapOf(
        "1" to genItemPreview(
            id = "1",
            from = listOf("2", "3"),
            into = List(7) { "1" },
        ),
        "2" to genItemPreview(
            id = "2",
            from = listOf("5", "6", "7"),
        ),
        "3" to genItemPreview(
            id = "3",
            from = listOf("4"),
        ),
        "4" to genItemPreview(id = "4"),
        "5" to genItemPreview(id = "5"),
        "6" to genItemPreview(id = "6"),
        "7" to genItemPreview(id = "7"),
    )
    AsyncImagePreviewProvider {
        Surface {
            ItemPopUpContent(
                item = map["1"]!!,
                itemsMap = map,
                version = "1.0.0",
                scrollToItem = {}
            )
        }
    }
}
