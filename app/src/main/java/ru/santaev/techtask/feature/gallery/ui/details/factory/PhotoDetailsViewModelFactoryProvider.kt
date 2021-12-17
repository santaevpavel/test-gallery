package ru.santaev.techtask.feature.gallery.ui.details.factory

import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import ru.santaev.techtask.feature.gallery.ui.details.PhotoDetailsViewModel
import ru.santaev.techtask.utils.ProviderViewModelFactory
import javax.inject.Inject

class PhotoDetailsViewModelFactoryProvider @Inject constructor(
    private val factory: PhotoDetailsViewModelAssistedFactory
) {
    fun create(
        photoId: String,
    ): ViewModelProvider.Factory {
        return ProviderViewModelFactory {
            factory.create(photoId)
        }
    }

    @AssistedFactory
    interface PhotoDetailsViewModelAssistedFactory {
        fun create(
            @Assisted photoId: String,
        ): PhotoDetailsViewModel
    }
}
