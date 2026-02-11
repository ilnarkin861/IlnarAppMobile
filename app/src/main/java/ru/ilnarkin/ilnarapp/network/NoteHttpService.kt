package ru.ilnarkin.ilnarapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteFilter


interface NoteHttpService {

	@GET("notes")
	suspend fun getAll(
		@Query("offset") offset: Int,
		@Query("limit") limit: Int,
		@Query("filter") filter: NoteFilter?
	): Response<AppPagination<Note>>


	@POST("notes/add")
	suspend fun create(@Body note: Note): Response<Note>


	@GET("notes/{id}")
	suspend fun getById(@Path("id") id: String): Response<Note?>


	@PUT("notes/edit/{id}")
	suspend fun update(@Path("id") id: String,@Body note: Note): Response<Note>


	@DELETE("notes/delete/{id}")
	suspend fun delete(@Path("id") id: String): Response<Any>
}