package org.lifetrack.ltapp.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.lifetrack.ltapp.model.network.KtorHttpClient
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.SupportPresenter

val koinModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl()
    }
    single{
        KtorHttpClient.create()
    }
    viewModelOf(::AlmaPresenter)
    viewModelOf(::AuthPresenter)
    viewModelOf(::SupportPresenter)
    viewModelOf(::ChatPresenter)


}

