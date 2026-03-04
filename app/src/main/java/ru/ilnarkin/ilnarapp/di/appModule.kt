package ru.ilnarkin.ilnarapp.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ilnarkin.ilnarapp.helpers.API_URL
import ru.ilnarkin.ilnarapp.helpers.PREFS_NAME
import ru.ilnarkin.ilnarapp.helpers.USER_ENDPOINT
import ru.ilnarkin.ilnarapp.repositories.ArchiveRepository
import ru.ilnarkin.ilnarapp.repositories.NoteRepository
import ru.ilnarkin.ilnarapp.repositories.NoteTypeRepository
import ru.ilnarkin.ilnarapp.repositories.TagRepository
import ru.ilnarkin.ilnarapp.repositories.UserRepository
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteFilterViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteTypeViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteViewModel
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {

	single<SharedPreferences> {
		androidContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}

	viewModel { UserViewModel(get(), get()) }
	viewModel { TagViewModel(get()) }
	viewModel { ArchiveViewModel(get()) }
	viewModel { NoteTypeViewModel(get()) }
	viewModel { NoteViewModel(get()) }
	viewModel { NoteFilterViewModel() }

	single { TagRepository(get()) }
	single { ArchiveRepository(get()) }
	single { NoteTypeRepository(get()) }
	single { NoteRepository(get()) }
	single { UserRepository(get(), "$API_URL/$USER_ENDPOINT") }
}