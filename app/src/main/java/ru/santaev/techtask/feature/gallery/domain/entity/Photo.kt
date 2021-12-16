package ru.santaev.techtask.feature.gallery.domain.entity

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String,
    val previewUrl: String
)