package com.example.musicappmvp.utils

import android.content.ContentResolver
import com.example.musicappmvp.data.SongLocalDataSource
import com.example.musicappmvp.data.SongRepository

object Repository {
    fun getSongRepository(contentResolver: ContentResolver): SongRepository {
        val local = SongLocalDataSource.getRepository(contentResolver)
        return SongRepository.getRepository(local)
    }
}
