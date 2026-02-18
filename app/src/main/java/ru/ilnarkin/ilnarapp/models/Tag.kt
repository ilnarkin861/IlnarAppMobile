package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class Tag (
	var id: String = "",
	var title: String
) : AppModel