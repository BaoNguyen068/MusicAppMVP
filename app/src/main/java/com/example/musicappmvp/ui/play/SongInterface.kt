package com.example.musicappmvp.ui.play

import com.example.musicappmvp.model.Song

interface SongInterface {

    interface View {
        fun updateAdapter(listSong: List<Song>)
        fun showError(error: String)
    }

    interface Presenter {
        fun getSongFromLocal()
    }

}
