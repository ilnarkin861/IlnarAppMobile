package ru.ilnarkin.ilnarapp.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import java.io.IOException


class NetworkErrorInterceptor(private val errorManager: NetworkErrorManager) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		try {
			val response = chain.proceed(chain.request())

			when (response.code){
				502 -> runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
				503 -> runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
				401 -> runBlocking { errorManager.notifyError(NetworkErrorType.UNAUTHORIZED) }
			}

			return response

		} catch (e: IOException) {
			runBlocking { errorManager.notifyError(NetworkErrorType.NO_INTERNET) }
			throw e
		}
	}
}