package ru.santaev.techtask.feature.gallery.ui.gallery

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import ru.santaev.techtask.feature.gallery.domain.PhotoInteractor
import ru.santaev.techtask.feature.gallery.ui.gallery.entity.PhotoUiEntity
import ru.santaev.techtask.utils.BaseViewModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModel @Inject constructor(
    interactor: PhotoInteractor
) : BaseViewModel() {

    // TODO use custom pagination logic instead of pagination lib
    val photos = interactor.getPhotos(previewSize = 300)
        .map { pagingData ->
            pagingData.map { PhotoUiEntity(it.id, it.previewUrl) }
        }
        .cachedIn(viewModelScope)
}
