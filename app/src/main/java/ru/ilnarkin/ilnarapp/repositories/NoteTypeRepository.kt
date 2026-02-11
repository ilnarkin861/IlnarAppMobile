package ru.ilnarkin.ilnarapp.repositories

import retrofit2.Response
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.network.NoteTypeHttpService


class NoteTypeRepository(private val httpService: NoteTypeHttpService) : Repository<NoteType, AppPagination<NoteType>, Nothing> {
	override suspend fun getList(
		offset: Int,
		limit: Int,
		filter: Nothing?
	): Response<AppPagination<NoteType>> {
		return httpService.getAll(offset, limit)
	}

	override suspend fun getById(id: String): Response<NoteType?> {
		TODO("Not yet implemented")
	}

	override suspend fun create(model: NoteType): Response<NoteType> {
		TODO("Not yet implemented")
	}

	override suspend fun update(model: NoteType): Response<NoteType> {
		TODO("Not yet implemented")
	}

	override suspend fun delete(id: String): Response<Any> {
		TODO("Not yet implemented")
	}
}