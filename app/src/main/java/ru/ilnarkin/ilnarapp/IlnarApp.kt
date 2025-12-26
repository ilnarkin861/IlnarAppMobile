package ru.ilnarkin.ilnarapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.ilnarkin.ilnarapp.di.appModule
import ru.ilnarkin.ilnarapp.di.networkModule


class IlnarApp : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@IlnarApp)
			modules(appModule, networkModule)
		}
	}
}