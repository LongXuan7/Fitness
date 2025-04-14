package com.example.fitness.util.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    protected fun launchWithErrorHandling(
        showLoading: Boolean = true,
        block: suspend () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            _isLoading.postValue(false)
            onError(exception)
        }

        viewModelScope.launch(handler) {
            try {
                if (showLoading) _isLoading.postValue(true)
                block()
            } catch (e: Exception) {
                onError(e)
            } finally {
                if (showLoading) _isLoading.postValue(false)
            }
        }
    }
}