package org.lifetrack.ltapp.di

import org.koin.dsl.module
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.ui.view.AuthView
import org.lifetrack.ltapp.presenter.AuthPresenter

val koinModule = module {
    single {
        object: AuthView {
            override fun showLoading(isLoading: Boolean, msg: String?) {
//                TODO("Not yet implemented")
            }

            override fun onAuthSuccess() {
//                TODO("Not yet implemented")
            }

            override fun onAuthSuccessWithData(data: String) {
//                TODO("Not yet implemented")
            }

            override fun showError(msg: String) {
//                TODO("Not yet implemented")
            }

        }
    }

    single<AuthRepository> {
        AuthRepositoryImpl()
    }

    single {
        AuthPresenter(
            get(),
            get()
        )
    }
}

