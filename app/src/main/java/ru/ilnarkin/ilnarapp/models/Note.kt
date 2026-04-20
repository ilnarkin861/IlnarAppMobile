package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data  class Note (
	var id: String = "",
	var title: String? = "",
	var text: String,
	var noteType: NoteType,
	var date: String,
	var archive: Archive? = null,
	var tags: MutableList<Tag> = mutableListOf(),
	var noteImages: MutableList<FileInfo> = mutableListOf(),
) : AppModel