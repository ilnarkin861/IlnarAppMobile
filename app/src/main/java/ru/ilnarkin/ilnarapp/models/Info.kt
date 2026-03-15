package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class Info (
    var success: Boolean,
    var messages: MutableList<String>
)