package ru.ilnarkin.ilnarapp.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException


class NetworkErrorInterceptor(private val errorManager: NetworkErrorManager, private val context: Context) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val response = chain.proceed(chain.request())

		try {

			if (!response.isSuccessful) {
				runBlocking { errorManager.notifyError(NetworkErrorType.NO_INTERNET) }
			}

			when (response.code){
				502 -> runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
				503 -> runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
				401 -> runBlocking { errorManager.notifyError(NetworkErrorType.UNAUTHORIZED) }
			}

		}

		catch (e: ConnectException) {
			runBlocking { errorManager.notifyError(NetworkErrorType.NO_INTERNET) }
			throw e
		}

		catch (e: SocketTimeoutException) {
			runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
			throw e

		}

		catch (e: IOException) {
			runBlocking { errorManager.notifyError(NetworkErrorType.NO_INTERNET) }
			throw e
		}

		return response
	}
}