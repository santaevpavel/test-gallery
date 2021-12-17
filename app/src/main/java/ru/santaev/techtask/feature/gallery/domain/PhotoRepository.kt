package ru.santaev.techtask.feature.gallery.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.santaev.techtask.di.modules.NetworkModule
import ru.santaev.techtask.network.api.PhotoApiService
import ru.santaev.techtask.network.entities.PhotoApiEntity
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoApi: PhotoApiService
) {

    suspend fun getPhoto(photoId: String): PhotoApiEntity {
        return withContext(Dispatchers.IO) {
            photoApi.getPhoto(photoId)
        }
    }

    fun getPhotosPagingSource(): Flow<PagingData<PhotoApiEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource() }
        ).flow
    }

    private suspend fun getPhotos(
        page: Int?,
        limit: Int?
    ): List<PhotoApiEntity> {
        return withContext(Dispatchers.IO) {
            photoApi.getPhotos(page, limit)
        }
    }

    fun getPhotoUrl(id: String, size: Int): String {
        return NetworkModule.BASE_URL + "id/$id/$size/$size"
    }

    inner class PhotoPagingSource : PagingSource<Int, PhotoApiEntity>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoApiEntity> {
            val pageIndex = params.key ?: PHOTOS_START_IDX
            return try {
                val photos = getPhotos(pageIndex, params.loadSize)
                LoadResult.Page(
                    data = photos,
                    prevKey = null,
                    nextKey = pageIndex + 1
                )
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, PhotoApiEntity>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }

    companion object {
        private const val PHOTOS_START_IDX = 1
    }
}