package ru.ilnarkin.ilnarapp.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


val appModule = module {

	viewModel { UserViewModel(get(), get() ) }

}