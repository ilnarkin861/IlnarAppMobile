package ru.ilnarkin.ilnarapp.repositories

import retrofit2.Response
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.FilterModel
import ru.ilnarkin.ilnarapp.network.ArchiveHttpService


class ArchiveRepository(private val httpService: ArchiveHttpService) : Repository<Archive, AppPagination<Archive>> {

	override suspend fun getList(
		offset: Int,
		limit: Int,
		filter: FilterModel?
	): Response<AppPagination<Archive>> {
		return httpService.getAll(offset, limit)
	}

	override suspend fun getById(id: String): Response<Archive?> {
		return httpService.getById(id)
	}

	override suspend fun create(model: Archive): Response<Archive> {
		return httpService.create(model)
	}

	override suspend fun update(model: Archive): Response<Archive> {
		return httpService.update(model.id, model)
	}

	override suspend fun delete(id: String): Response<Any> {
		return httpService.delete(id)
	}
}