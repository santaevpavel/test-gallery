package ru.santaev.techtask.di

import ru.santaev.techtask.di.modules.AppModule
import ru.santaev.techtask.di.modules.NetworkModule
import ru.santaev.techtask.di.modules.ViewModelModule
import dagger.Component
import ru.santaev.techtask.feature.gallery.ui.UserCreationFragment
import ru.santaev.techtask.feature.gallery.ui.UsersFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelModule::class, NetworkModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(module: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(arg: UsersFragment)
    fun inject(arg: UserCreationFragment)
}
