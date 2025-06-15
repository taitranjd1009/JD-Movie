package com.example.jdmovie.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun HideStatusBar() {
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    LaunchedEffect(Unit) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            WindowInsetsControllerCompat(it, it.decorView).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}