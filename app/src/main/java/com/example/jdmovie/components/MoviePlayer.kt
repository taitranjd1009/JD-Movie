package com.example.jdmovie.components

import android.app.Activity
import android.net.Uri
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun MoviePlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    useController: Boolean = false
) {
    val context = LocalContext.current
//    val activity = context as Activity

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            prepare()
            playWhenReady = true
        }
    }

    val playerView = remember {
        PlayerView(context).apply {
            player = exoPlayer
            this.useController = useController
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,

            )
            setFullscreenButtonClickListener {  }

        }
    }

    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    AndroidView(
        factory = { playerView },
        modifier = modifier
    )
}
