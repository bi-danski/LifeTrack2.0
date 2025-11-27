package org.lifetrack.ltapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.lifetrack.ltapp.model.database.LTRoomDatabase
import org.lifetrack.ltapp.model.network.KtorHttpClient
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.model.repository.ChatRepository
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.AnalyticPresenter
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
    single {
        Room.databaseBuilder(
            androidContext(),
            LTRoomDatabase::class.java,
            "lifetrack_db"
            ).build()
    }
    single {
        get<LTRoomDatabase>().chatDao()
    }
    single {
        ChatRepository(get())
    }
    viewModelOf(::AlmaPresenter)
    viewModelOf(::AuthPresenter)
    viewModelOf(::SupportPresenter)
//    viewModelOf(::ChatPresenter)
    viewModelOf(::AnalyticPresenter)
    viewModel {
        (savedStateHandle: SavedStateHandle) ->
        ChatPresenter(get(), savedStateHandle)
    }
}

