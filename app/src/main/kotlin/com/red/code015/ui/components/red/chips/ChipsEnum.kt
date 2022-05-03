package com.red.code015.ui.components.red.chips

import androidx.compose.runtime.Composable

data class ChipsDataEnum<E : Enum<E>>(val id: E, val text: String)

fun <T : Enum<T>> Map<T, String>.toListChips(): List<ChipsDataEnum<T>> =
    map { ChipsDataEnum(it.key, it.value) }

@Composable
private fun <E : Enum<E>> List<ChipsDataEnum<E>>.ChipsEnumBase(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onFilterChipClick: (E?) -> Unit = {},
) {
    map { ChipsData(it.id, it.text) }.ChipsBase(selected, title, nullOption, onFilterChipClick)
}

@Composable
fun <E : Enum<E>> Map<E, String>.ChipsEnum(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onClick: (E?) -> Unit = {},
) {
    toListChips().ChipsEnumBase(selected, title, nullOption, onClick)
}

@Composable
private fun <E : Enum<E>> Array<E>.ChipsEnum(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onClick: (E?) -> Unit = {},
) {
    toList().ChipsEnum(selected, title, nullOption, onClick)
}

@Composable
private fun <E : Enum<E>> Collection<E>.ChipsEnum(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onClick: (E?) -> Unit = {},
) {
    toList().map { ChipsDataEnum(it, it.name) }.ChipsEnumBase(selected, title, nullOption, onClick)
}

@Composable
fun <E : Enum<E>> Map<E, String>.ChipsEnumSafe(
    selected: E,
    title: String? = null,
    onClick: (E) -> Unit = {},
) {
    toListChips().ChipsEnumBase(selected, title, false) { it?.let { onClick(it) } }
}

@Composable
private fun <E : Enum<E>> Array<E>.ChipsEnumSafe(
    selected: E,
    title: String? = null,
    onClick: (E) -> Unit = {},
) {
    toList().ChipsEnum(selected, title, false) { it?.let { onClick(it) } }
}

@Composable
private fun <E : Enum<E>> Collection<E>.ChipsEnumSafe(
    selected: E,
    title: String? = null,
    onClick: (E) -> Unit = {},
) {
    toList().map { ChipsDataEnum(it, it.name) }
        .ChipsEnumBase(selected, title, false) { it?.let { onClick(it) } }
}
