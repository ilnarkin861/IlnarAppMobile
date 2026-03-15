package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class Pagination(
	val count: Int,
	val hasPreviousPage: Boolean,
	val hasNextPage: Boolean
)