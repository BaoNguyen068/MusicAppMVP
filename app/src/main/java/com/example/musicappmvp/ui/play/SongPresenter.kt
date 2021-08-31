package com.example.musicappmvp.ui.play

import android.util.Log
import com.example.musicappmvp.data.OnDataLocalCallback
import com.example.musicappmvp.data.SongRepository
import com.example.musicappmvp.model.Song

class SongPresenter(private val view: SongInterface.View, private val repository: SongRepository) :
    SongInterface.Presenter {
    override fun getSongFromLocal() {
        repository.getAllSong(object : OnDataLocalCallback<List<Song>> {
            override fun onSucceed(data: List<Song>) {
                view.updateAdapter(data)
            }

            override fun onFailed(e: Exception?) {
                view.showError(e?.message.toString())
            }
        })
    }

}
