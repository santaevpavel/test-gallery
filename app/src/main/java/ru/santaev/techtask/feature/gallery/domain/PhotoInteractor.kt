package ru.santaev.techtask.feature.gallery.domain

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.santaev.techtask.feature.gallery.domain.entity.Photo
import ru.santaev.techtask.network.entities.PhotoApiEntity
import javax.inject.Inject

class PhotoInteractor @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    fun getPhotos(previewSize: Int): Flow<PagingData<Photo>> {
        return photoRepository.getPhotosPagingSource()
            .map { pagingData ->
                pagingData.map { photo ->
                    Photo(
                        id = photo.id,
                        author = photo.author,
                        width = photo.width,
                        height = photo.height,
                        url = photo.url,
                        downloadUrl = photo.downloadUrl,
                        previewUrl = photoRepository.getPhotoUrl(photo.id, previewSize)
                    )
                }
            }
    }
}
