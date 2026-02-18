package ru.ilnarkin.ilnarapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.interceptors.AuthInterceptor
import ru.ilnarkin.ilnarapp.interceptors.NetworkErrorInterceptor
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.network.TokenManager
import java.net.ConnectException


val networkModule = module {

	single { NetworkErrorManager() }
	single { TokenManager(get()) }

	single { NetworkErrorInterceptor(get(), get()) }
	single { AuthInterceptor(get()) }


	single {
		val errorManager: NetworkErrorManager = get()
		val tokenManager: TokenManager = get()
		val context = androidContext()

		HttpClient(CIO) {

			install(ContentNegotiation) { json(
				Json {
					ignoreUnknownKeys = true
					prettyPrint = true
					isLenient = true
				})
			}

			expectSuccess = true

			defaultRequest {

				header(HttpHeaders.ContentType, ContentType.Application.Json)

				val token = tokenManager.getAuthToken()

				if (!token.isNullOrBlank()) {
					header("Authorization", "Bearer $token")
				}
			}

			HttpResponseValidator {


				handleResponseExceptionWithRequest { cause, _ ->
					val networkAvailable = isNetworkAvailable(context)

					if (cause is ApiException) {
						errorManager.notifyError(NetworkErrorType.SERVER_ERROR)
					}

					if (cause is ClientRequestException){
						val status = cause.response.status

						if (networkAvailable && status == HttpStatusCode.Unauthorized) {
							errorManager.notifyError(NetworkErrorType.UNAUTHORIZED)
						}

						if (status == HttpStatusCode.NotFound){
							throw ApiException("Ресурс не найден")
						}
					}

					if(cause is HttpRequestTimeoutException || cause is ConnectException) {
						if (networkAvailable) errorManager.notifyError(NetworkErrorType.SERVER_ERROR)
						else errorManager.notifyError(NetworkErrorType.NO_INTERNET)
					}
				}
			}
		}
	}
}

private fun isNetworkAvailable(context: Context): Boolean {
	val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val network = connectivityManager.activeNetwork ?: return false
	val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
	return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
			capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
			capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}