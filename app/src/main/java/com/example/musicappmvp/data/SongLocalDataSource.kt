package com.example.musicappmvp.data

import android.content.ContentResolver
import android.provider.MediaStore
import android.util.Log
import com.example.musicappmvp.model.Song

class SongLocalDataSource (private val contentResolver: ContentResolver) : SongDataSource {

    override fun getAllSong(callback: OnDataLocalCallback<List<Song>>) {
        LocalAsyncTask<Unit, List<Song>>(callback) {
            getSongFromDevice()
        }.execute(Unit)
    }

    private fun getSongFromDevice(): List<Song> {
        val listSong = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                listSong.add(Song(it))
            }
        }
        cursor?.close()
        return listSong
    }

    companion object {
        private var instance: SongLocalDataSource? = null
        fun getRepository(contentResolver: ContentResolver) =
            instance ?: SongLocalDataSource(contentResolver).also { instance = it }
    }
}
