package ru.santaev.techtask.feature.gallery.ui.details

import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.santaev.techtask.feature.gallery.domain.PhotoInteractor
import ru.santaev.techtask.utils.BaseViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoDetailsViewModel @AssistedInject constructor(
    @Assisted private val photoId: String,
    private val interactor: PhotoInteractor
) : BaseViewModel() {

    // TODO cache photos and get it instantly from repository
    val photo = flow {
        emit(interactor.getPhoto(photoId))
    }
        .map { it.downloadUrl }
        .catch { err ->
            Timber.e(err) { "Error while loading photo" }
            // TODO handle error
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
