package com.example.musicappmvp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicappmvp.R
import com.example.musicappmvp.service.SongService

class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Get action from event OnClickPending in Notification
        var actionMusic =
                intent?.getIntExtra(context?.getString(R.string.action_intent_music_service), 0)
        // Send action from Broadcast to Service
        var intentReceiver =
                Intent(context, SongService::class.java)
        intentReceiver.putExtra(context?.getString(R.string.action_intent_music_receiver), actionMusic)
        context?.startService(intentReceiver)
    }
}
