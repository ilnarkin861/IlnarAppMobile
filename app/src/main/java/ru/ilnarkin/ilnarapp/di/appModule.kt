package ru.ilnarkin.ilnarapp.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ilnarkin.ilnarapp.helpers.PREFS_NAME
import ru.ilnarkin.ilnarapp.repositories.ArchiveRepository
import ru.ilnarkin.ilnarapp.repositories.TagRepository
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


val appModule = module {

	single<SharedPreferences> {
		androidContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}

	viewModel { UserViewModel(get(), get() ) }
	viewModel { TagViewModel(get()) }
	viewModel { ArchiveViewModel(get()) }

	single { TagRepository(get()) }
	single { ArchiveRepository(get()) }
}