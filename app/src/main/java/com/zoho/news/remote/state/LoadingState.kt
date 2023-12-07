package com.zoho.news.remote.state

sealed class LoadingState<out T> {
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error(val message: String) : LoadingState<Nothing>()
    data object Loading : LoadingState<Nothing>()
    data object Empty : LoadingState<Nothing>()
}