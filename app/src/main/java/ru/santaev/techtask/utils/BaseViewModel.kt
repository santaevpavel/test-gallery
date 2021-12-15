package ru.santaev.techtask.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


abstract class BaseViewModel : ViewModel(), ToastPublisher {

    private val _toast = MutableStateFlow<Toast?>(null)
    val toast: Flow<Event<Toast>> = _toast.map { toast -> toast?.let { Event(it) } }
        .filterNotNull()

    private val _progress = MutableStateFlow(false)
    val progress = _progress.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    override fun showToast(toast: Toast) {
        _toast.tryEmit(toast)
    }

    suspend fun <T> runWithProgress(block: suspend () -> T) {
        try {
            _progress.value = true
            block.invoke()
        } finally {
            _progress.value = false
        }
    }
}
