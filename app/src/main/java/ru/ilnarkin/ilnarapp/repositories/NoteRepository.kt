package ru.ilnarkin.ilnarapp.repositories

import retrofit2.Response
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.network.NoteHttpService


class NoteRepository(private val httpService: NoteHttpService) : Repository<Note, AppPagination<Note>, NoteFilter> {

	override suspend fun getList(
		offset: Int,
		limit: Int,
		filter: NoteFilter?
	): Response<AppPagination<Note>> {
		return httpService.getAll(offset, limit, filter)
	}

	override suspend fun getById(id: String): Response<Note?> {
		return httpService.getById(id)
	}

	override suspend fun create(model: Note): Response<Note> {
		return httpService.create(model)
	}

	override suspend fun update(model: Note): Response<Note> {
		return httpService.update(model.id, model)
	}

	override suspend fun delete(id: String): Response<Any> {
		return httpService.delete(id)
	}
}