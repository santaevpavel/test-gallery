package ru.santaev.techtask.utils

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

fun <T> tickerFlow(interval: Long, initialDelay: Long = interval, block: () -> T): Flow<T> {
    return flow {
        delay(initialDelay)
        emit(block())
        while (currentCoroutineContext().isActive) {
            delay(interval)
            emit(block())
        }
    }
}