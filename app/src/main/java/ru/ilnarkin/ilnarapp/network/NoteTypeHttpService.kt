package ru.ilnarkin.ilnarapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.NoteType


interface NoteTypeHttpService {

	@GET("notetypes")
	suspend fun getAll(
		@Query("offset") offset: Int,
		@Query("limit") limit: Int
	): Response<AppPagination<NoteType>>
}