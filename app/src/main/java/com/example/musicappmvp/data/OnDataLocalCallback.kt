package com.example.musicappmvp.data

interface OnDataLocalCallback<T> {
    fun onSucceed(data: T)
    fun onFailed(e: Exception?)
}
