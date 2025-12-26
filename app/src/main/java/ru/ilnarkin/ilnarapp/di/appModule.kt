package ru.ilnarkin.ilnarapp.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.ilnarkin.ilnarapp.helpers.PREFS_NAME


val appModule = module {
	single<SharedPreferences> {
		androidContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}

}