package ru.santaev.techtask.di

import dagger.Component
import ru.santaev.techtask.di.modules.AppModule
import ru.santaev.techtask.di.modules.NetworkModule
import ru.santaev.techtask.di.modules.ViewModelModule
import ru.santaev.techtask.feature.gallery.ui.details.PhotoDetailsFragment
import ru.santaev.techtask.feature.gallery.ui.gallery.GalleryFragment
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

    fun inject(arg: GalleryFragment)
    fun inject(arg: PhotoDetailsFragment)
}
