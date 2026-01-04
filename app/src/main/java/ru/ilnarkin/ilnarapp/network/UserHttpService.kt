package ru.ilnarkin.ilnarapp.network

import retrofit2.Response
import retrofit2.http.GET


interface UserHttpService {

	@GET("user/auth-check")
	suspend fun checkAuth() : Response<Any>
}