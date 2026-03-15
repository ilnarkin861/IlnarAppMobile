package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class Token(
    var token: String
) : AppModel
