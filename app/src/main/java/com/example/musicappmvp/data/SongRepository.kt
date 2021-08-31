package com.example.musicappmvp.data

import com.example.musicappmvp.model.Song
class SongRepository (private val local: SongDataSource) : SongDataSource {

    override fun getAllSong(callback: OnDataLocalCallback<List<Song>>) {
        local.getAllSong(callback)
    }

    companion object {
        private var repository: SongRepository? = null
        fun getRepository(local: SongDataSource) =
            repository ?: SongRepository(local).also { repository = it }
    }
}
