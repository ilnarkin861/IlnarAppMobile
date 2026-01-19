package ru.ilnarkin.ilnarapp.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.interceptors.AuthInterceptor
import ru.ilnarkin.ilnarapp.interceptors.NetworkErrorInterceptor
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.network.TagHttpService
import ru.ilnarkin.ilnarapp.network.TokenManager
import ru.ilnarkin.ilnarapp.network.UserHttpService


val networkModule = module {

	single { NetworkErrorManager() }

	single { NetworkErrorInterceptor(get(), get()) }

	single { AuthInterceptor(get()) }

	single { TokenManager(androidContext()) }

	single { get<Retrofit>().create(UserHttpService::class.java) }
	single { get<Retrofit>().create(TagHttpService::class.java) }

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
}