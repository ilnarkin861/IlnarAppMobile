package ru.ilnarkin.ilnarapp.interceptors

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import ru.ilnarkin.ilnarapp.helpers.KEY_TOKEN


class AuthInterceptor(private val prefs: SharedPreferences) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()

		val token = prefs.getString(KEY_TOKEN, null)

		val requestBuilder = originalRequest.newBuilder()

		if (token != null) {
			requestBuilder.addHeader("Authorization", "Bearer $token")
		}

		return chain.proceed(requestBuilder.build())
	}
}