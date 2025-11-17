package com.brasuka.tv

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.ui.PlayerView

@UnstableApi
class PlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var channelNameTextView: TextView
    
    private var channelId: Int = 0
    private var channelName: String = ""
    private var channelUrl: String = ""
    
    private val channels = ChannelProvider.getDemoChannels()
    private var currentChannelIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        channelNameTextView = findViewById(R.id.channelNameTextView)

        // Ocultar la barra de sistema para experiencia inmersiva
        hideSystemUI()

        channelId = intent.getIntExtra("CHANNEL_ID", 0)
        channelName = intent.getStringExtra("CHANNEL_NAME") ?: ""
        channelUrl = intent.getStringExtra("CHANNEL_URL") ?: ""
        
        // Encontrar el índice actual del canal
        currentChannelIndex = channels.indexOfFirst { it.id == channelId }
        if (currentChannelIndex == -1) currentChannelIndex = 0

        channelNameTextView.text = channelName

        initializePlayer()
    }

    private fun initializePlayer() {
        loadingIndicator.visibility = View.VISIBLE
        
        // Crear el DataSource Factory para streaming
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(30000)
            .setReadTimeoutMs(30000)

        // Crear ExoPlayer
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer
                
                // Crear MediaItem
                val mediaItem = MediaItem.fromUri(channelUrl)
                exoPlayer.setMediaItem(mediaItem)
                
                // Listener para manejar estados
                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_READY -> {
                                loadingIndicator.visibility = View.GONE
                            }
                            Player.STATE_BUFFERING -> {
                                loadingIndicator.visibility = View.VISIBLE
                            }
                            Player.STATE_ENDED -> {
                                loadingIndicator.visibility = View.GONE
                            }
                            Player.STATE_IDLE -> {
                                loadingIndicator.visibility = View.GONE
                            }
                        }
                    }

                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        loadingIndicator.visibility = View.GONE
                        Toast.makeText(
                            this@PlayerActivity,
                            "Error al reproducir: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                
                // Preparar y reproducir
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
    }

    private fun changeChannel(newIndex: Int) {
        if (newIndex in channels.indices) {
            currentChannelIndex = newIndex
            val newChannel = channels[currentChannelIndex]
            
            channelUrl = newChannel.url
            channelName = newChannel.name
            channelNameTextView.text = channelName
            
            // Liberar el reproductor actual y crear uno nuevo
            releasePlayer()
            initializePlayer()
            
            Toast.makeText(this, "Canal: ${newChannel.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_CHANNEL_UP -> {
                // Canal siguiente
                changeChannel(currentChannelIndex + 1)
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_CHANNEL_DOWN -> {
                // Canal anterior
                changeChannel(currentChannelIndex - 1)
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            exoPlayer.release()
        }
        player = null
    }

    override fun onStart() {
        super.onStart()
        if (player == null) {
            initializePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}
