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

    suspend fun getPhoto(photoId: String): Photo {
        return photoRepository.getPhoto(photoId).mapToPhoto(previewSize = 0)
    }

    fun getPhotos(previewSize: Int): Flow<PagingData<Photo>> {
        return photoRepository.getPhotosPagingSource()
            .map { pagingData ->
                pagingData.map { photo ->
                    photo.mapToPhoto(previewSize)
                }
            }
    }

    private fun PhotoApiEntity.mapToPhoto(
        previewSize: Int
    ): Photo {
        return Photo(
            id = id,
            author = author,
            width = width,
            height = height,
            url = url,
            downloadUrl = downloadUrl,
            previewUrl = photoRepository.getPhotoUrl(id, previewSize)
        )
    }
}
