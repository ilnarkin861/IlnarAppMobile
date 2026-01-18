package ru.ilnarkin.ilnarapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.ilnarkin.ilnarapp.models.Token
import ru.ilnarkin.ilnarapp.models.UserLoginData


interface UserHttpService {

	@GET("user/auth-check")
	suspend fun checkAuth() : Response<Any>


	@POST("user/login")
	suspend fun login(@Body userAuthData : UserLoginData) : Response<Token>
}