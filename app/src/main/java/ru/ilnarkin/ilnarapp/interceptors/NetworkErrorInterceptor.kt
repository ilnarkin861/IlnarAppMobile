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


class NetworkErrorInterceptor(private val errorManager: NetworkErrorManager, private val context: Context) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {

		try {

			if (!isNetworkAvailable()){
				throw NoConnectivityException()
			}

			val response = chain.proceed(chain.request())

			when (response.code){
				502 -> runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
				503 -> runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
				401 -> runBlocking { errorManager.notifyError(NetworkErrorType.UNAUTHORIZED) }
			}

			return response
		}

		catch (e: NoConnectivityException){
			runBlocking { errorManager.notifyError(NetworkErrorType.NO_INTERNET) }
			throw e
		}

		catch (e: IOException){
			runBlocking { errorManager.notifyError(NetworkErrorType.SERVER_ERROR) }
			throw e
		}
	}


	private fun isNetworkAvailable(): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val network = connectivityManager.activeNetwork ?: return false
		val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
		return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
	}
}


class NoConnectivityException : IOException()