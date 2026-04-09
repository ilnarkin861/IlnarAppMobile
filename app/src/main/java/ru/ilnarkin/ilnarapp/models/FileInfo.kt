package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class FileInfo(
	var id: String,
	var url: String,
	var createdAt: String
): AppModel
