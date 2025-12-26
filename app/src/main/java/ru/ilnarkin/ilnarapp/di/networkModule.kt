package ru.ilnarkin.ilnarapp.di

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.interceptors.AuthInterceptor
import ru.ilnarkin.ilnarapp.interceptors.NetworkErrorInterceptor
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager

val networkModule = module {

	single { NetworkErrorManager() }

	single { NetworkErrorInterceptor(get()) }

	single { AuthInterceptor(get()) }

	single {
		OkHttpClient.Builder()
			.addInterceptor(get<NetworkErrorInterceptor>())
			.addInterceptor(get<AuthInterceptor>())
			.build()
	}

	single {
		Retrofit.Builder()
			.baseUrl(API_URL)
			.client(get())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}
}