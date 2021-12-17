package ru.santaev.techtask.feature.gallery.domain

import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import ru.santaev.techtask.factory.PhotoFactory
import ru.santaev.techtask.network.api.PhotoApiService

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoPagingSourceTest {

    @RelaxedMockK
    private lateinit var photoApi: PhotoApiService
    private lateinit var repository: PhotoRepository
    private lateinit var source: PhotoRepository.PhotoPagingSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = PhotoRepository(photoApi)
        source = repository.PhotoPagingSource()
    }

    @Test
    fun `should correctly load first page`(): Unit = runBlockingTest {
        // Arrange
        val photos = (0 until 5).map { PhotoFactory.createPhotoApiEntity(id = "$it") }
        coEvery { photoApi.getPhotos(page = 1, limit = 2) } returns photos.subList(0, 2)
        coEvery { photoApi.getPhotos(page = 2, limit = 2) } returns photos.subList(2, 4)
        coEvery { photoApi.getPhotos(page = 3, limit = 2) } returns emptyList()
        // Act
        val result = source.load(
            Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )
        // Assert
        result shouldBe Page(
            data = listOf(photos[0], photos[1]),
            prevKey = null,
            nextKey = 2
        )
    }
}