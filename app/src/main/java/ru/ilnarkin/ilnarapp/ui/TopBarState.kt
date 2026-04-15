package ru.ilnarkin.ilnarapp.ui


data class TopBarState(
	val title: String = "",
	val showBackButton: Boolean = false,
	val isSelectionMode: Boolean = false,
	val onBackClick: () -> Unit = {},
	val onChangeClick: () -> Unit = {},
	val onDeleteSelectedClick: () -> Unit = {}
)