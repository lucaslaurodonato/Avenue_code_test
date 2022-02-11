package com.lucasdonato.sicredi_bank_events.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lucasdonato.sicredi_bank_events.data.remote.WebServiceClient
import com.lucasdonato.sicredi_bank_events.data.remote.dataSource.EventsDataSource
import com.lucasdonato.sicredi_bank_events.data.repository.events.EventsRepository
import com.lucasdonato.sicredi_bank_events.data.useCase.EventsUseCase
import com.lucasdonato.sicredi_bank_events.mechanism.location.LocationUtils
import com.lucasdonato.sicredi_bank_events.mechanism.location.MapManager
import com.lucasdonato.sicredi_bank_events.mechanism.permission.AppPermissionUtils
import com.lucasdonato.sicredi_bank_events.mechanism.permission.PermissionListener
import com.lucasdonato.sicredi_bank_events.ui.details.viewModel.EventDetailViewModel
import com.lucasdonato.sicredi_bank_events.ui.home.viewmodel.HomeViewModel
import com.lucasdonato.sicredi_bank_events.ui.onboarding.viewModel.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { EventDetailViewModel(get()) }
    viewModel { OnboardingViewModel() }
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
        viewModelModules,
        webServiceModules,
        mechanismModules,
        repositoryModules,
        dataSourceModules,
        useCaseModules
    )