package com.nei.ichigo.core.data.model

data class Config(
    val version: ConfigValue,
    val lang: ConfigValue
)

sealed class ConfigValue(val value: String) {
    class SelectedByUser(value: String) : ConfigValue(value)
    class AutomaticSelection(value: String) : ConfigValue(value)

    val byUser: String?
        get() = when (this) {
            is SelectedByUser -> value
            is AutomaticSelection -> null
        }
}