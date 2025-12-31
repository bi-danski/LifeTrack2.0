package org.lifetrack.ltapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import android.content.Context
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.lifetrack.ltapp.model.datastore.LTPreferenceSerializer
import org.lifetrack.ltapp.model.datastore.TokenPreferenceSerializer
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lifetrack.ltapp.model.roomdb.LTRoomDatabase
import org.lifetrack.ltapp.core.network.KtorHttpClient
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.model.repository.ChatRepository
import org.lifetrack.ltapp.model.repository.UserRepository
import org.lifetrack.ltapp.model.repository.UserRepositoryImpl
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.PrescPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.SettingsPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter


private val Context.tokenDataStore by dataStore(
    fileName = "_preferences.json",
    serializer = TokenPreferenceSerializer
)
private val Context.ltDataStore by dataStore(
    fileName = "lt_preferences.json",
    serializer = LTPreferenceSerializer
)

val koinModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    single(qualifier = named("tokenStore")){ androidContext().tokenDataStore }
    single(qualifier = named("ltStore")) { androidContext().ltDataStore }
    single {
        PreferenceRepository(
            ltDataStore = get( qualifier = named("ltStore")),
            tokenDataStore = get(qualifier = named("tokenStore")),
            scope = get()
        )
    }
    single<AuthRepository> {
        AuthRepositoryImpl(client = get(), prefs = get())
    }
    single<UserRepository> {
        UserRepositoryImpl(client = get(), prefRepository = get())
    }

    single<io.ktor.client.HttpClient>{ KtorHttpClient.create(get()) }
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = LTRoomDatabase::class.java,
            name = "lifetrack_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single {
        get<LTRoomDatabase>().chatDao()
    }
    single { ChatRepository(get()) }

    viewModelOf(::AuthPresenter)
    viewModelOf(::HomePresenter)
    viewModelOf(::UserPresenter)
    viewModelOf(::FUVPresenter)
    viewModelOf(::SettingsPresenter)
    viewModelOf(::SharedPresenter)
    viewModelOf(::AnalyticPresenter)
    viewModelOf(::PrescPresenter)
    viewModel {
        (savedStateHandle: SavedStateHandle) ->
        ChatPresenter(get(), savedStateHandle)
    }
}

