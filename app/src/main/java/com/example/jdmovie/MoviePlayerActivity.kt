package com.example.jdmovie

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING


var passedString: String = ""

class MoviePlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    passedString = intent.getStringExtra("movieKey").toString()
                    ExoPlayerDemo()
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ExoPlayerDemo() {
    val context = LocalContext.current
    val activity = context as Activity
    var player: Player? by remember {
        mutableStateOf(null)
    }

    val enterFullScreen = { activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE }
    val exitFullScreen = { activity.requestedOrientation = SCREEN_ORIENTATION_USER }

    val playerView = createPlayerView(player)
    activity.requestedOrientation == SCREEN_ORIENTATION_USER
    playerView.controllerAutoShow = true
    playerView.keepScreenOn = true
    playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
    playerView.setFullscreenButtonClickListener { isFullScreen ->
        with(context) {
            if (isFullScreen) {
                if (activity.requestedOrientation == SCREEN_ORIENTATION_USER) {
                    enterFullScreen()
                } else {
                    exitFullScreen()
                }
            }
        }
    }

    ComposableLifeCycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                player = initPlayer(context)
                playerView.onResume()
            }

            Lifecycle.Event.ON_RESUME -> {
                player = initPlayer(context)
                playerView.onResume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                playerView.apply {
                    player?.release()
                    onPause()
                    player = null
                }
            }

            Lifecycle.Event.ON_STOP -> {
                playerView.apply {
                    player?.release()
                    onPause()
                    player = null
                }
            }

            else -> {}
        }

    }

    AndroidView(factory = { playerView })
}

@OptIn(UnstableApi::class)
private fun initPlayer(context: Context): Player {
    return ExoPlayer.Builder(context).build().apply {
        val defaultHttpDataSource = DefaultHttpDataSource.Factory()
        var uri = Uri.parse(passedString)
        val mediaSource = buildMediaSource(uri, defaultHttpDataSource, null)
        setMediaSource(mediaSource)
        playWhenReady = true
        prepare()
    }
}

@OptIn(UnstableApi::class)
private fun buildMediaSource(
    uri: Uri?,
    defaultHttpDataSource: DefaultHttpDataSource.Factory,
    exception: String?
): MediaSource {
    val type = Util.inferContentType(uri!!, exception)

    return when (type) {
        C.CONTENT_TYPE_DASH -> DashMediaSource.Factory(defaultHttpDataSource!!).createMediaSource(
            MediaItem.fromUri(uri)
        )

        C.CONTENT_TYPE_SS -> SsMediaSource.Factory(defaultHttpDataSource!!).createMediaSource(
            MediaItem.fromUri(uri)
        )

        C.CONTENT_TYPE_HLS -> HlsMediaSource.Factory(defaultHttpDataSource!!).createMediaSource(
            MediaItem.fromUri(uri)
        )

        C.CONTENT_TYPE_OTHER -> ProgressiveMediaSource.Factory(defaultHttpDataSource!!)
            .createMediaSource(
                MediaItem.fromUri(uri)
            )

        else -> {
            throw IllegalStateException("Unsupported type: $type")
        }
    }
}

@Composable
fun ComposableLifeCycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@Composable
private fun createPlayerView(player: Player?): PlayerView {
    val context = LocalContext.current
    val playerView = remember {
        PlayerView(context).apply {
            this.player = player
        }
    }

    DisposableEffect(key1 = player) {
        playerView.player = player
        onDispose {
            playerView.player = null
        }
    }
    return playerView
}
