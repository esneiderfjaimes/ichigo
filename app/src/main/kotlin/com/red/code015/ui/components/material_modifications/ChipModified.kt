package com.red.code015.ui.components.material_modifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ChipColors
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun Chip(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = CircleShape,
    border: BorderStroke? = null,
    colors: ChipColors = ChipDefaults.chipColors(
        disabledBackgroundColor = MaterialTheme.colorScheme.primary,
    ),
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        modifier = modifier.defaultMinSize(
            minHeight = ChipDefaults.MinHeight
        ),
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(1.0f),
        border = border,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.bodyMedium
            ) {
                Row(
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = rememberRipple(),
                            enabled = enabled,
                            role = Role.Button,
                            onClick = onClick
                        )
                        .padding(
                            start = if (leadingIcon == null) {
                                HorizontalPadding
                            } else 0.dp,
                            end = HorizontalPadding,
                            top = VerticalPadding,
                            bottom = VerticalPadding
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        Spacer(Modifier.width(LeadingIconStartSpacing))
                        val leadingIconContentColor by colors.leadingIconContentColor(enabled)
                        CompositionLocalProvider(
                            LocalContentColor provides leadingIconContentColor,
                            LocalContentAlpha provides leadingIconContentColor.alpha,
                            content = leadingIcon
                        )
                        Spacer(Modifier.width(LeadingIconEndSpacing))
                    }
                    content()
                }
            }
        }
    }
}

/**
 * The content padding used by a chip.
 * Used as start padding when there's leading icon, used as eng padding when there's no
 * trailing icon.
 */
private val HorizontalPadding = 12.dp

/**
 * The content padding used by a chip.
 * Used as start padding when there's leading icon, used as eng padding when there's no
 * trailing icon.
 */
private val VerticalPadding = 2.dp

/**
 * The size of the spacing before the leading icon when they used inside a chip.
 */
private val LeadingIconStartSpacing = 4.dp

/**
 * The size of the spacing between the leading icon and a text inside a chip.
 */
private val LeadingIconEndSpacing = 8.dp