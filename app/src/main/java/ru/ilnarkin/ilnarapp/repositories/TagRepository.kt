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
		return httpService.getById(id)
	}

	override suspend fun create(model: Tag): Response<Tag> {
		return httpService.create(model)
	}

	override suspend fun update(model: Tag): Response<Tag> {
		return httpService.update(model.id, model)
	}

	override suspend fun delete(id: String): Response<Any> {
		return httpService.delete(id)
	}
}