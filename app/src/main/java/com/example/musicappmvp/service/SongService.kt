package com.example.musicappmvp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.*
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.musicappmvp.R
import com.example.musicappmvp.model.Song
import com.example.musicappmvp.notification.senNotification

@Suppress("UNREACHABLE_CODE")
class SongService : Service() {
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private var isPlayMusic: Boolean = false
    private  var updateSong: Song? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel(
                getString(R.string.info_channel_id),
                getString(R.string.info_channel_name)
        )
        val bundle = intent.extras
        if (bundle != null) {
            var song: Song? = bundle?.getParcelable(getString(R.string.action_intent_listsong))
            if (song != null) {
                updateSong = song
                startMusic(song)
                startForeground(
                        1,
                        senNotification(
                                this,
                                song,
                                isPlayMusic
                        )
                )
            }
        }
        // Get action fom Broadcast(ActionReceiver)
        var actionMusicFromReceiver: Int = intent.getIntExtra(getString(R.string.action_intent_music_receiver), 0)
        handleActionMusic(actionMusicFromReceiver)
        return START_NOT_STICKY
    }

    private fun startMusic(song: Song) {
        mediaPlayer?.apply {
            setDataSource(song.SongURL)
            prepare()
            start()
        }
        isPlayMusic = true
        sendActionToActivity(ACTION_START)
    }

    fun handleActionMusic(action: Int){
        when(action){
            ACTION_PAUSE -> pauseMusic()
            ACTION_RESUME -> resumeMusic()
            ACTION_CANCEL -> cancelMusic()
        }
    }

    private fun cancelMusic() {
        stopSelf()
        sendActionToActivity(ACTION_CANCEL)
    }

    fun updateSendNotification(){
        startForeground(
                1,
                senNotification(
                        this,
                        updateSong as Song,
                        isPlayMusic
                )
        )
    }

    private fun resumeMusic() {
        if (mediaPlayer != null && !isPlayMusic) {
            mediaPlayer?.start()
            isPlayMusic = true
            updateSendNotification() // Update view Notification
            sendActionToActivity(ACTION_RESUME)
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer != null && isPlayMusic) {
            mediaPlayer?.pause()
            isPlayMusic = false
            updateSendNotification()
            sendActionToActivity(ACTION_PAUSE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.stopForeground(true)
        cancelNotification()
        mediaPlayer?.stop()
        stopSelf()
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = getString(R.string.msg_remind)
            }
            val notificationManager = this.getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun cancelNotification() {
        val notificationManager = ContextCompat.getSystemService(
                this, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
    }

    fun sendActionToActivity(action: Int) {
        var intent = Intent(getString(R.string.action_intent_send_data_activity))
        var bundle = Bundle()
        bundle.apply {
            putParcelable(getString(R.string.object_song), updateSong)
            putBoolean(getString(R.string.status_isplay), isPlayMusic)
            putInt(getString(R.string.action_bundle_music), action)
        }
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val REQUEST_CODE = 0
        const val ACTION_PAUSE = 1
        const val ACTION_RESUME = 2
        const val ACTION_CANCEL = 3
        const val ACTION_START = 4
        const val ACTION_PREVIOUS = 5
        const val ACTION_NEXT = 6
    }

}
