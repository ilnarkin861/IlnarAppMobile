package ru.ilnarkin.ilnarapp.models


data class Pagination(
	val count: Int,
	val hasPreviousPage: Boolean,
	val hasNextPage: Boolean,
)