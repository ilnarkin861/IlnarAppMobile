package ru.ilnarkin.ilnarapp.repositories

import retrofit2.Response
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.FilterModel
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.network.TagHttpService


class TagRepository(private val httpService: TagHttpService) : Repository<Tag, AppPagination<Tag>> {
	override suspend fun getList(
		offset: Int,
		limit: Int,
		filter: FilterModel?
	): Response<AppPagination<Tag>> {
		return httpService.getAll(offset, limit)
	}

	override suspend fun getById(id: String): Response<Tag?> {
		TODO("Not yet implemented")
	}

	override suspend fun create(model: Tag): Response<Tag> {
		TODO("Not yet implemented")
	}

	override suspend fun update(model: Tag): Response<Tag> {
		TODO("Not yet implemented")
	}

	override suspend fun delete(id: String): Response<Any> {
		TODO("Not yet implemented")
	}
}