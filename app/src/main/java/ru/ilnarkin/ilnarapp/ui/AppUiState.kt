package ru.ilnarkin.ilnarapp.ui

import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.models.Pagination


data class AppUiState<T> (
	var success: Boolean = false,
	var isAuth: Boolean = false,
	var loading: Boolean = false,
	var data: T? = null,
	var offset: Int = 0,
	var list: List<T> = listOf(),
	var showAlert: Boolean = false,
	var pagination: Pagination? = null,
	var message: String = "",
	var actionType: ActionType = ActionType.READ
)