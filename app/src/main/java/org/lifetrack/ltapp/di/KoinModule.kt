package org.lifetrack.ltapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import android.content.Context
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.lifetrack.ltapp.core.datastore.LTPreferenceSerializer
import org.lifetrack.ltapp.core.datastore.TokenPreferenceSerializer
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lifetrack.ltapp.model.database.room.LTRoomDatabase
import org.lifetrack.ltapp.model.network.KtorHttpClient
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.model.repository.ChatRepository
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.EPrescriptPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.SettingsPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.other.SnackbarManager


private val Context.tokenDataStore by dataStore(
    fileName = "_preferences.json",
    serializer = TokenPreferenceSerializer
)

private val Context.ltDataStore by dataStore(
    fileName = "lt_preferences.json",
    serializer = LTPreferenceSerializer
)

val koinModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Main) }

    single(qualifier = named("tokenStore")){ androidContext().tokenDataStore }
    single(qualifier = named("ltStore")) { androidContext().ltDataStore }
    single {
        PreferenceRepository(
            ltDataStore = get( qualifier = named("ltStore")),
            tokenDataStore = get(qualifier = named("tokenStore")),
            scope = get()
        )
    }
    single<AuthRepository> { AuthRepositoryImpl(client = get(), prefs = get()) }
    single{ KtorHttpClient.create(get()) }
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = LTRoomDatabase::class.java,
            name = "lifetrack_db"
            ).build()
    }
    single {
        get<LTRoomDatabase>().chatDao()
    }
    single { ChatRepository(get()) }
    single { SnackbarManager() }

    viewModelOf(::AuthPresenter)
    viewModelOf(::HomePresenter)
    viewModelOf(::UserPresenter)
    viewModelOf(::FUVPresenter)
    viewModelOf(::SettingsPresenter)
    viewModelOf(::SharedPresenter)
    viewModelOf(::AnalyticPresenter)
    viewModelOf(::EPrescriptPresenter)
    viewModel {
        (savedStateHandle: SavedStateHandle) ->
        ChatPresenter(get(), savedStateHandle)
    }
}

