package com.example.musicappmvp.ui.play

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicappmvp.R
import com.example.musicappmvp.data.SongLocalDataSource
import com.example.musicappmvp.model.Song
import com.example.musicappmvp.service.SongService
import com.example.musicappmvp.ui.adapter.songAdapter
import com.example.musicappmvp.utils.Repository


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), songAdapter.onItemClickListener, SongInterface.View {
    private var songPresenter: SongPresenter? = null
    private var listSong = mutableListOf<Song>()
    private var adapter = songAdapter(this)
    private var isPlaying: Boolean = false
    private var song: Song? = null

    private  var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            var bundle = intent.extras
            if (bundle == null) {
                return
            }
            song = bundle?.getParcelable(getString(R.string.object_song))
            isPlaying = bundle?.getBoolean(getString(R.string.status_isplay))
            var acitionMusicFromService: Int = bundle?.getInt(getString(R.string.action_bundle_music))
            handleLayoutMusic(acitionMusicFromService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, IntentFilter(getString(R.string.action_intent_send_data_activity)))
        if (checkPermission()) {
            loadSong()
        } else requestPermission()
    }

    fun stopForceGroundService(){
        var intent: Intent = Intent(this, SongService::class.java)
        stopService(intent)
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            )
                return false
        }
        return true
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 111
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSong()
        }
    }

    fun loadSong(){
        songPresenter = SongPresenter(this, Repository.getSongRepository(contentResolver))
        songPresenter?.getSongFromLocal()
        recycler_listsong.layoutManager = LinearLayoutManager(this)
        recycler_listsong.adapter = adapter
    }

    override fun updateAdapter(Songs: List<Song>) {
        listSong = Songs as MutableList<Song>
        adapter.updateData(Songs)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(position: Int) {
        stopForceGroundService()
        var intent: Intent = Intent(this,SongService::class.java)
        var bundle: Bundle = Bundle()
        bundle.putParcelable(getString(R.string.action_intent_listsong), listSong[position])
        intent.putExtras(bundle)
        startService(intent)
    }

    fun handleLayoutMusic(action: Int) {
        SongService.apply {
            when(action){
                ACTION_START -> startMusic()
                ACTION_PAUSE -> pauseMusic()
                ACTION_RESUME -> resumeMusic()
                ACTION_CANCEL -> cancelMusic()
            }
        }
    }

    private fun startMusic() {
        linearlayout_bottom.visibility = View.VISIBLE
        showSongToLayoutBottom()
        setStatusButtonControl()
    }

    private fun cancelMusic() {
        linearlayout_bottom.visibility = View.GONE
    }

    private fun resumeMusic() {
        setStatusButtonControl()
    }

    private fun pauseMusic() {
        setStatusButtonControl()
    }

    fun showSongToLayoutBottom() {
        textview_title_song_home.text = song?.Title
        textview_author_song_home.text = song?.Author
        button_prev_home.setImageResource(R.drawable.ic_previous)
        button_next_home.setImageResource(R.drawable.ic_next)
        setOnClickPauseOrPlayLayoutBottom()
        setOnClickCancelLayoutBottom()
    }

    fun setStatusButtonControl() {
        if (isPlaying) {
            button_pause_home.setImageResource(R.drawable.ic_pause)
        } else {
            button_pause_home.setImageResource(R.drawable.ic_play)
        }
    }

    fun setOnClickPauseOrPlayLayoutBottom() {
        button_pause_home.setOnClickListener {
            if (isPlaying) {
                sendActionToService(SongService.ACTION_PAUSE)
            } else {
                sendActionToService(SongService.ACTION_RESUME)
            }
        }
    }

    fun setOnClickCancelLayoutBottom() {
        button_cancel_home.setOnClickListener {
            sendActionToService(SongService.ACTION_CANCEL)
        }
    }

    fun sendActionToService(action: Int) {
        var intent: Intent = Intent(this, SongService::class.java)
        intent.putExtra(getString(R.string.action_intent_music_receiver), action)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

}
