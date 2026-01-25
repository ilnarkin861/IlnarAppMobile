package ru.ilnarkin.ilnarapp.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.ilnarkin.ilnarapp.network.TokenManager


class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {

		val requestBuilder = chain.request().newBuilder()

		val token = tokenManager.getAuthToken()

		if (!token.isNullOrEmpty()){
			requestBuilder.addHeader("Authorization", "Bearer $token")
		}

		return chain.proceed(requestBuilder.build())
	}
}