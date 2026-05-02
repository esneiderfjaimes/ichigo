package com.nei.ichigo.core.data.model

data class Config(
    val version: ConfigValue,
    val lang: ConfigValue
)

sealed class ConfigValue(open val value: String) {
    data class SelectedByUser(override val value: String) : ConfigValue(value)
    data class AutomaticSelection(override val value: String) : ConfigValue(value)

    val byUser: String?
        get() = when (this) {
            is SelectedByUser -> value
            is AutomaticSelection -> null
        }
}