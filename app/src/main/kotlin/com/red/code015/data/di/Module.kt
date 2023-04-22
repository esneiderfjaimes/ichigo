package com.red.code015.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.red.code015.data.AppSettings
import com.red.code015.data.AppSettingsSerializer
import com.red.code015.data.repositories.SummonerRepository
import com.red.code015.ui.IchigoAppViewModel
import com.red.code015.ui.pages.encyclopedia.EncyclopediaViewModel
import com.red.code015.ui.pages.encyclopedia.champions.ChampionsViewModel
import com.red.code015.ui.pages.home.screens.home.HomeViewModel
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel
import com.red.code015.ui.pages.home.screens.summoner.SummonerViewModel
import com.red.code015.ui.pages.home.screens.summoner.masteries.MasteriesViewModel
import com.red.code015.ui.pages.masteries_and_chests.MasteriesAndChestsViewModel
import com.red.code015.usecases.ChampionsRotationsUserCase
import com.red.code015.usecases.EncyclopediaChampionUserCase
import com.red.code015.usecases.MasteriesUserCase
import com.red.code015.usecases.ProfileByRiotIDUserCase
import com.red.code015.usecases.ProfileBySummonerNameUserCase
import com.red.code015.usecases.SummonerByPuuIDUserCase
import com.red.code015.usecases.SummonerBySummonerNameUserCase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val Context.dataStore: DataStore<AppSettings>
        by dataStore("app-settings.json", AppSettingsSerializer)

val appModule = module {
    single { androidApplication().dataStore }
    single { SummonerRepository(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { IchigoAppViewModel(get()) }
    viewModel { ChampionsViewModel(get(), get()) }
    viewModel { EncyclopediaViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MasteriesViewModel(get(), get()) }
    viewModel { MasteriesAndChestsViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get()) }
    viewModel { SummonerViewModel(get(), get()) }

}
val userCaseModule = module {
    singleOf(::EncyclopediaChampionUserCase)
    singleOf(::ProfileByRiotIDUserCase)
    singleOf(::ChampionsRotationsUserCase)
    singleOf(::ProfileBySummonerNameUserCase)
    singleOf(::SummonerByPuuIDUserCase)
    singleOf(::SummonerBySummonerNameUserCase)
    singleOf(::MasteriesUserCase)
}