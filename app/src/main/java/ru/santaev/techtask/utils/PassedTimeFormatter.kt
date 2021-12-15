package ru.santaev.techtask.utils

fun formatPassedTime(time: Long): String {
    val millisInSecond = 1_000
    val millisInMinute = 60 * millisInSecond
    val timePassed = (System.currentTimeMillis() - time).coerceAtLeast(0L)
    val seconds = timePassed / millisInSecond
    val minutes = timePassed / millisInMinute
    return when {
        minutes <= 0 -> "${seconds}s"
        else -> "${minutes}m"
    }
}