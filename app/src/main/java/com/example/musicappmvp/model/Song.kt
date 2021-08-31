package com.example.musicappmvp.model

import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize

@Parcelize
class Song( var  SongURL: String, var Title: String, var Author: String) : Parcelable {

    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)),
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST))
    )

}
