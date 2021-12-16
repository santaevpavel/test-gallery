package ru.santaev.techtask.feature.gallery.domain

import ru.santaev.techtask.feature.gallery.domain.entity.Photo
import ru.santaev.techtask.network.entities.PhotoApiEntity
import javax.inject.Inject

class PhotoInteractor @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend fun getPhotos(page: Int?, limit: Int?, previewSize: Int): List<Photo> {
        return photoRepository.getPhotos(page, limit)
            .map { apiEntity ->
                Photo(
                    id = apiEntity.id,
                    author = apiEntity.author,
                    width = apiEntity.width,
                    height = apiEntity.height,
                    url = apiEntity.url,
                    downloadUrl = apiEntity.downloadUrl,
                    previewUrl = photoRepository.getPhotoUrl(apiEntity.id, previewSize)
                )
            }
    }
}
