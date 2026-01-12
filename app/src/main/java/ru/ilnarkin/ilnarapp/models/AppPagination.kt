package ru.ilnarkin.ilnarapp.models


data class AppPagination<TModel>(
	val data: MutableList<TModel>,
	val pagination: Pagination
): PaginationModel where TModel : AppModel