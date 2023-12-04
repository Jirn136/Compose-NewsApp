package com.zoho.news.remote.state

sealed class LoadingState<out T> {
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error(val message: String) : LoadingState<Nothing>()
    object Loading : LoadingState<Nothing>()
}