package ru.santaev.techtask.factory

import ru.santaev.techtask.network.entities.PhotoApiEntity

object PhotoFactory {

    fun createPhotoApiEntity(
        id: String = StringFactory.create(),
        author: String = StringFactory.create(),
        width: Int = 100,
        height: Int = 100,
        url: String = "",
        downloadUrl: String = "",
    ): PhotoApiEntity {
        return PhotoApiEntity(
            id = id,
            author = author,
            width = width,
            height = height,
            url = url,
            downloadUrl = downloadUrl
        )
    }
}