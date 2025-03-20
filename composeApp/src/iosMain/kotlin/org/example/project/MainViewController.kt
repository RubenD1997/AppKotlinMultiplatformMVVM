package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import org.example.db.AppDatabase
import org.example.project.data.DatabaseDriverFactory
import org.example.project.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(appModule(AppDatabase.invoke(DatabaseDriverFactory().createDriver())))
    }.koin
}