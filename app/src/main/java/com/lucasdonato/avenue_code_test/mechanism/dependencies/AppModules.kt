package com.lucasdonato.avenue_code_test.mechanism.dependencies

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lucasdonato.avenue_code_test.data.remote.WebServiceClient
import com.lucasdonato.avenue_code_test.data.remote.dataSource.EventsDataSource
import com.lucasdonato.avenue_code_test.data.repository.events.EventsRepository
import com.lucasdonato.avenue_code_test.data.useCase.EventsUseCase
import com.lucasdonato.avenue_code_test.mechanism.location.LocationUtils
import com.lucasdonato.avenue_code_test.mechanism.location.MapManager
import com.lucasdonato.avenue_code_test.mechanism.permission.AppPermissionUtils
import com.lucasdonato.avenue_code_test.mechanism.permission.PermissionListener
import com.lucasdonato.avenue_code_test.presentation.details.presenter.DetailsPresenter
import com.lucasdonato.avenue_code_test.presentation.home.presenter.HomePresenter
import com.lucasdonato.avenue_code_test.presentation.onboarding.presenter.OnboardingPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presenterModules = module {
    factory { HomePresenter(get()) }
    factory { DetailsPresenter(get()) }
    factory { OnboardingPresenter(androidContext()) }
}

val useCaseModules = module {
    factory { EventsUseCase(get()) }
}

val repositoryModules = module {
    factory { EventsRepository(get()) }
}

val dataSourceModules = module {
    factory { EventsDataSource(get()) }
}

val webServiceModules = module {
    single { WebServiceClient().webService }
}

val mechanismModules = module {
    factory { (context: Context) -> MapManager(context, get()) }
    factory { (activity: AppCompatActivity, listener: PermissionListener) ->
        AppPermissionUtils(activity, listener)
    }
    single { LocationUtils(androidContext()) }
}

val applicationModules =
    listOf(
        presenterModules, useCaseModules, repositoryModules, dataSourceModules,
        webServiceModules, mechanismModules
    )