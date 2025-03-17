package org.example.project.di

import org.example.project.data.ExpenseManager
import org.example.project.data.ExpensesRepositoryImpl
import org.example.project.domain.ExpensesRepository
import org.example.project.presentation.ExpensesViewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

fun appModule() = module {
    single {
        ExpenseManager
    }.withOptions { createdAtStart() }
    single<ExpensesRepository> { ExpensesRepositoryImpl(get()) }
    factory { ExpensesViewModel(get()) }
}