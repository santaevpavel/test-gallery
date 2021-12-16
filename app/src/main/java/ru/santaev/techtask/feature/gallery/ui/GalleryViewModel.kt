package ru.santaev.techtask.feature.gallery.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import ru.santaev.techtask.feature.gallery.domain.PhotoInteractor
import ru.santaev.techtask.feature.gallery.ui.entity.PhotoUiEntity
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
