package ru.ilnarkin.ilnarapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Tag


interface TagHttpService {

	@GET("tags")
	suspend fun getAll(
		@Query("offset") offset: Int,
		@Query("limit") limit: Int
	): Response<AppPagination<Tag>>


	@POST("tags/add")
	suspend fun create(@Body tag: Tag): Response<Tag>


	@GET("tags/{id}")
	suspend fun getById(@Path("id") id: String): Response<Tag?>
}