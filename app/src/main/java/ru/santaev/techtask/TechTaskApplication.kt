package ru.santaev.techtask

import android.app.Application
import ru.santaev.techtask.di.AppComponent
import ru.santaev.techtask.di.DaggerAppComponent
import ru.santaev.techtask.di.modules.AppModule
import timber.log.Timber

class TechTaskApplication : Application() {

    val component: AppComponent by lazy { createBuilder().build() }

    private fun createBuilder(): AppComponent.Builder {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
