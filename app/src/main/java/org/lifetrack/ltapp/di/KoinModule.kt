package org.lifetrack.ltapp.di

import android.content.Context
import androidx.datastore.dataStore
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.lifetrack.ltapp.core.network.KtorHttpClient
import org.lifetrack.ltapp.core.service.AlmaService
import org.lifetrack.ltapp.model.datastore.LTPreferenceSerializer
import org.lifetrack.ltapp.model.datastore.TokenPreferenceSerializer
import org.lifetrack.ltapp.model.datastore.UserPreferenceSerializer
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.model.repository.ChatRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.lifetrack.ltapp.model.repository.UserRepository
import org.lifetrack.ltapp.model.repository.UserRepositoryImpl
import org.lifetrack.ltapp.model.roomdb.LTRoomDatabase
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.PrescPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter

private val Context.tokenDataStore by dataStore(
    fileName = "_prefs.json",
    serializer = TokenPreferenceSerializer
)

private val Context.ltDataStore by dataStore(
    fileName = "lt_prefs.json",
    serializer = LTPreferenceSerializer
)

private val Context.userDataStore by dataStore(
    fileName = "_u_prefs.json",
    serializer = UserPreferenceSerializer
)

val koinModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single(qualifier = named("tokenStore")){ androidContext().tokenDataStore }
    single(qualifier = named("ltStore")) { androidContext().ltDataStore }
    single(qualifier = named("userStore")) { androidContext().userDataStore}
    single { AlmaService(get()) }
    single {
        PreferenceRepository(
            ltDataStore = get( qualifier = named("ltStore")),
            tokenDataStore = get(qualifier = named("tokenStore")),
            userDataStore = get(qualifier = named("userStore")),
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
        ).fallbackToDestructiveMigration(false).build()
    }
    single {
        get<LTRoomDatabase>().chatDao()
    }
    single { ChatRepository(get()) }

    viewModelOf(::AuthPresenter)
    viewModelOf(::HomePresenter)
    viewModelOf(::UserPresenter)
    viewModelOf(::FUVPresenter)
    viewModelOf(::SharedPresenter)
    viewModelOf(::PrescPresenter)
    viewModel {
        (savedStateHandle: SavedStateHandle) ->
        ChatPresenter(
            get(),
            savedStateHandle,
            get()
        )
    }
}