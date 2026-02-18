package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class NoteFilter (
	var noteTypeId: String,
	var year: Int?,
	var month: Int?,
	var day: Int?,
	var archiveId: String?,
	var tagIds: MutableList<String>? = mutableListOf()
) : FilterModel