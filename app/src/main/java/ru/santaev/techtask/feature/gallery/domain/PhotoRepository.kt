package ru.santaev.techtask.feature.gallery.domain

import ru.santaev.techtask.di.modules.NetworkModule
import ru.santaev.techtask.network.api.PhotoApiService
import ru.santaev.techtask.network.entities.PhotoApiEntity
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoApi: PhotoApiService
) {

    suspend fun getPhotos(
        page: Int?,
        limit: Int?
    ): List<PhotoApiEntity> {
        return photoApi.getPhotos(page, limit)
    }

    fun getPhotoUrl(id: String, size: Int): String {
        return NetworkModule.BASE_URL + "id/$id/$size/$size"
    }
}