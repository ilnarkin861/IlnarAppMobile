package ru.ilnarkin.ilnarapp.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.interceptors.AuthInterceptor
import ru.ilnarkin.ilnarapp.interceptors.NetworkErrorInterceptor
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.network.UserHttpService


val networkModule = module {

	single { NetworkErrorManager() }

	single { NetworkErrorInterceptor(get()) }

	single { AuthInterceptor(get()) }

	single {
		OkHttpClient.Builder()
			.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
			.addInterceptor(get<AuthInterceptor>())
			.addInterceptor(get<NetworkErrorInterceptor>())
			.build()
	}

	single {
		Retrofit.Builder()
			.baseUrl(API_URL)
			.client(get())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	single { get<Retrofit>().create(UserHttpService::class.java) }
}