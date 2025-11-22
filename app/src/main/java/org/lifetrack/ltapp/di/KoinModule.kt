package org.lifetrack.ltapp.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter


val koinModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl()
    }

    viewModelOf(::AlmaPresenter)
    viewModelOf(::AuthPresenter)
}

