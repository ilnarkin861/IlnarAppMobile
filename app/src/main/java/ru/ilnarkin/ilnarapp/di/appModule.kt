package ru.ilnarkin.ilnarapp.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ilnarkin.ilnarapp.repositories.TagRepository
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


val appModule = module {

	viewModel { UserViewModel(get(), get() ) }
	viewModel { TagViewModel(get()) }

	single { TagRepository(get()) }
}