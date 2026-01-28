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
import ru.ilnarkin.ilnarapp.models.Archive


interface ArchiveHttpService {

	@GET("archives")
	suspend fun getAll(
		@Query("offset") offset: Int,
		@Query("limit") limit: Int
	): Response<AppPagination<Archive>>


	@POST("archives/add")
	suspend fun create(@Body archive: Archive): Response<Archive>


	@GET("archives/{id}")
	suspend fun getById(@Path("id") id: String): Response<Archive?>


	@PUT("archives/edit/{id}")
	suspend fun update(@Path("id") id: String,@Body archive: Archive): Response<Archive>


	@DELETE("archives/delete/{id}")
	suspend fun delete(@Path("id") id: String): Response<Any>
}