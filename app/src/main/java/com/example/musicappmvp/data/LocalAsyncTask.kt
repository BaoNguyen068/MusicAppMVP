package com.example.musicappmvp.data

import android.os.AsyncTask

class LocalAsyncTask<V, T>(
    private val callback: OnDataLocalCallback<T>,
    private val handler: (V) -> T
) : AsyncTask<V, Unit, T>() {

    private var exception: Exception? = null

    override fun onPostExecute(result: T?) {
        super.onPostExecute(result)
        result?.let {
            callback.onSucceed(result)
        } ?: callback.onFailed(exception)
    }

    override fun doInBackground(vararg p0: V): T? =
        try {
            handler(p0.first())
        } catch (e: Exception) {
            exception = e
            null
        }

    companion object {
        const val NUMBER_ZERO = 0
    }
}

