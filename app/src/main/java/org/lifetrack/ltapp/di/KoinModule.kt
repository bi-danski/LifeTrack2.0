package org.lifetrack.ltapp.di

import android.content.Context
import androidx.datastore.dataStore
import androidx.lifecycle.SavedStateHandle
import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import androidx.room.RoomDatabase
import io.kotzilla.sdk.KotzillaSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asExecutor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
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
import org.lifetrack.ltapp.network.KtorHttpClient
import org.lifetrack.ltapp.network.NetworkObserver
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.PrescPresenter
import org.lifetrack.ltapp.presenter.SessionManager
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.TLinePresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.service.AlmaService

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

@OptIn(ExperimentalRoomApi::class)
val koinModule = module {
    single(named("koinScope"), createdAtStart = false) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single(named("tokenStore"), createdAtStart = false) {
        androidContext().tokenDataStore
    }

    single(named("ltStore"), createdAtStart = false) {
        androidContext().ltDataStore
    }

    single(named("userStore"), createdAtStart = false) {
        androidContext().userDataStore
    }

    single(createdAtStart = false) {
        Room.databaseBuilder(
            androidContext(),
            LTRoomDatabase::class.java,
            "lifetrack_db"
        )
            .setQueryExecutor(Dispatchers.IO.asExecutor())
            .setTransactionExecutor(Dispatchers.IO.asExecutor())
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .fallbackToDestructiveMigration(false)
            .setInMemoryTrackingMode(true)
            .build()
    }
    single(createdAtStart = false) {
        get<LTRoomDatabase>().chatDao()
    }

    single(createdAtStart = false) {
        NetworkObserver(
            androidContext(),
            get(named("koinScope"))
        )
    }
    KotzillaSDK.trace("KtorHttpClient::Trace") {
        single(createdAtStart = false) {
            KtorHttpClient.create(
                get(),
                get(),
                get(named("koinScope"))
            )
        }
    }
    KotzillaSDK.trace("PreferenceRepository::Trace") {
        single(createdAtStart = false) {
            PreferenceRepository(
                get(named("ltStore")),
                get(named("tokenStore")),
                get(named("userStore")),
                get(named("koinScope"))
            )
        }
    }
    single<AuthRepository>(createdAtStart = false) {
        AuthRepositoryImpl(get(), get())
    }

    single<UserRepository>(createdAtStart = false) {
        UserRepositoryImpl(get(), get())
    }

    single(createdAtStart = false) {
        ChatRepository(get())
    }

    KotzillaSDK.trace("SessionManager::Trace") {
        single(createdAtStart = false) {
            SessionManager(
                get(),
                get(),
                get(named("koinScope"))
            )
        }
    }
    single(createdAtStart = false) {
        AlmaService(get())
    }

    viewModelOf(::AuthPresenter)
    viewModelOf(::HomePresenter)
    viewModelOf(::UserPresenter)
    viewModelOf(::FUVPresenter)
    viewModelOf(::SharedPresenter)
    viewModelOf(::PrescPresenter)
    viewModelOf(::TLinePresenter)
    viewModel { (handle: SavedStateHandle) ->
        ChatPresenter(
            get(),
            handle,
            get()
        )
    }
}
