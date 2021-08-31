package com.example.musicappmvp.data

import com.example.musicappmvp.model.Song
interface SongDataSource {
    fun getAllSong(callback: OnDataLocalCallback<List<Song>>)
}
