package org.example.project

import android.app.Application
import org.example.db.AppDatabase
import org.example.project.data.DatabaseDriverFactory
import org.example.project.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApp)
            androidLogger()
            modules(appModule(AppDatabase.Companion.invoke(DatabaseDriverFactory(this@MainApp).createDriver())))
        }
    }
}