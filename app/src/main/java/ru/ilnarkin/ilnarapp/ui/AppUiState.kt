package ru.ilnarkin.ilnarapp.ui


data class AppUiState<T> (
	var success: Boolean = false,
	var isAuth: Boolean = false,
	var data: T? = null,
)