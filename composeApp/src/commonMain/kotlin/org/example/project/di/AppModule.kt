package org.example.project.di

import org.example.db.AppDatabase
import org.example.project.core.HttpClientProvider
import org.example.project.data.ExpenseManager
import org.example.project.data.ExpensesRepositoryImpl
import org.example.project.domain.ExpensesRepository
import org.example.project.presentation.ExpensesViewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

fun appModule(appDatabase: AppDatabase) = module {
    single {
        ExpenseManager
    }.withOptions { createdAtStart() }
    single { HttpClientProvider.create() }
    single<ExpensesRepository> { ExpensesRepositoryImpl(get(), appDatabase, get()) }
    factory { ExpensesViewModel(get()) }
}