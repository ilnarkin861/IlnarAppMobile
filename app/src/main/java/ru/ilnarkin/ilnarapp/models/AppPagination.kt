package ru.ilnarkin.ilnarapp.models

import kotlinx.serialization.Serializable


@Serializable
data class AppPagination<TModel>(
	val data: MutableList<TModel>,
	val pagination: Pagination
): PaginationModel where TModel : AppModel