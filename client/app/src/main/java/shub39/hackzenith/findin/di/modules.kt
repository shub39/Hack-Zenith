package shub39.hackzenith.findin.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import shub39.hackzenith.findin.viewmodel.AuthViewModel
import shub39.hackzenith.findin.viewmodel.MainPageViewModel
import shub39.hackzenith.findin.viewmodel.User

val appModule = module {
    single { User() }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { MainPageViewModel(get(), androidContext().contentResolver) }
}