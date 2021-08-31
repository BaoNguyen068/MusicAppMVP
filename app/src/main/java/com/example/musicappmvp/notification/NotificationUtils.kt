package com.example.musicappmvp.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.musicappmvp.R
import com.example.musicappmvp.model.Song
import com.example.musicappmvp.service.SongService
import com.example.musicappmvp.ui.play.MainActivity

fun senNotification(context: Context, song: Song, isPlayMusic: Boolean): Notification {
    var intent = Intent(context, MainActivity::class.java)
    var pendingIntent = PendingIntent.getActivity(context,
            SongService.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    var remoteViews = RemoteViews(context.packageName, R.layout.notifi_custom)
    remoteViews.apply {
        setTextViewText(R.id.textview_title_song, song.Title)
        setTextViewText(R.id.textview_author_song, song.Author)
        setImageViewResource(R.id.button_prev, R.drawable.ic_previous)
        setImageViewResource(R.id.button_next, R.drawable.ic_next)
        setImageViewResource(R.id.button_pause, R.drawable.ic_pause)
        setImageViewResource(R.id.button_cancel, R.drawable.ic_quit)
    }
    if (isPlayMusic) {
        remoteViews.setOnClickPendingIntent(R.id.button_pause, getPendingIntent(context, SongService.ACTION_PAUSE))
        remoteViews.setImageViewResource(R.id.button_pause, R.drawable.ic_pause)
    } else {
        remoteViews.setOnClickPendingIntent(R.id.button_pause, getPendingIntent(context, SongService.ACTION_RESUME))
        remoteViews.setImageViewResource(R.id.button_pause, R.drawable.ic_play)
    }
    remoteViews.apply {
        setOnClickPendingIntent(R.id.button_cancel, getPendingIntent(context, SongService.ACTION_CANCEL))
        setOnClickPendingIntent(R.id.button_next, getPendingIntent(context, SongService.ACTION_NEXT))
        setOnClickPendingIntent(R.id.button_prev, getPendingIntent(context, SongService.ACTION_PREVIOUS))
    }

    return NotificationCompat.Builder(context, context.getString(R.string.info_channel_id))
            .setSmallIcon(R.drawable.image_item_song)
            .setContentIntent(pendingIntent)
            .setContent(remoteViews)
            .setSound(null)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()
}

fun getPendingIntent(context: Context, action: Int): PendingIntent {
    var intent = Intent(context, ActionReceiver::class.java)
    intent.putExtra(context.getString(R.string.action_intent_music_service), action)
    return PendingIntent.getBroadcast(context.applicationContext, action, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}
