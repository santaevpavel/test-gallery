package ru.santaev.techtask.feature.gallery.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.santaev.techtask.feature.gallery.domain.PhotoInteractor
import ru.santaev.techtask.feature.gallery.ui.entity.PhotoUiEntity
import ru.santaev.techtask.utils.BaseViewModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModel @Inject constructor(
    private val interactor: PhotoInteractor
) : BaseViewModel() {

    val photos = flow {
        emit(interactor.getPhotos(page = 0, limit = 50, previewSize = 300))
    }
        .map { photos ->
            photos.map { PhotoUiEntity(it.id, it.previewUrl) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
