package ru.ilnarkin.ilnarapp.models

import android.net.Uri


data class SelectedFileInfo(
	val uri: Uri,
	val name: String,
	val mimeType: String?
)
