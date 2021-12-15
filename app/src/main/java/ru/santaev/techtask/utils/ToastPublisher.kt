package ru.santaev.techtask.utils


interface ToastPublisher {

    fun showToast(toast: Toast)
}

fun ToastPublisher.showToast(message: String) {
    showToast(Toast(message))
}

class Toast(
    val message: String,
    val isLong: Boolean = false
)

