package ru.santaev.techtask.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.santaev.techtask.network.entities.PhotoApiEntity

interface PhotoApiService {

    @GET("v2/list")
    suspend fun getPhotos(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): List<PhotoApiEntity>
}
